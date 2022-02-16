package org.md.jmeter.csv.results.writer;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.md.jmeter.csv.results.writer.csv.ResultRow;
import org.md.jmeter.csv.results.writer.csv.Writer;
import org.md.jmeter.csv.results.writer.result.Result;
import org.md.jmeter.csv.results.writer.result.ResultContext;
import org.md.jmeter.csv.results.writer.result.ResultController;
import org.md.jmeter.csv.results.writer.result.ResultCreator;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.md.jmeter.csv.results.writer.config.Settings.Parameters.*;

/**
 * Backend listener that writes JMeter metrics to csv file, the idea writes the failed requests and their details.
 * @author Michail Derevyanko
 */
public class CsvBackendListenerClient extends AbstractBackendListenerClient implements Runnable {

    /**
     * Parameter Keys.
     */
    private Timer timer;
    private ScheduledThreadPoolExecutor scheduler;
    private boolean recordSubSamples;
    private final ResultContext resultContext = new ResultContext();
    private Set<String> samplersToFilter;
    private String regexForSamplerList;

    private static final int ONE_MS_IN_NANOSECONDS = 1000000;
    /**
     * Random number generator.
     */
    private Random randomNumberGenerator;

    /**
     * Logger.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CsvBackendListenerClient.class);

    /**
     * Gets default Parameters from JMeter UI.
     * @return {@link Arguments}.
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();
        arguments.addArgument(KEY_PATH, "the allowed format is -> C:\\jmeter_data.csv or /opt/results/jmeter_data.csv");
        arguments.addArgument(KEY_TIME_ZONE_ID, "UTC");
        arguments.addArgument(KEY_TIME_FORMAT, "yyyy-MM-dd'T'HH:mm:ss");
        arguments.addArgument(KEY_SEPARATOR, "|");
        arguments.addArgument(KEY_SAVE_OK_SAMPLERS, "false");
        arguments.addArgument(KEY_RECORD_SUB_SAMPLES, "true");
        arguments.addArgument(KEY_USE_REGEX_FOR_SAMPLER_LIST, "true");
        arguments.addArgument(KEY_SAMPLERS_LIST, ".*");
        arguments.addArgument(KEY_BATCH_SIZE, "2000");
        arguments.addArgument(KEY_FLUSH_INTERVAL, "10000");

        return arguments;
    }

    /**
     * Setups the test before while starting.
     * @param context represents {@link BackendListenerContext}.
     */
    @Override
    public void setupTest(BackendListenerContext context) {
        this.randomNumberGenerator = new Random();

        this.resultContext.setBackendListenerContext(context);
        this.resultContext.setTimeToSet(System.currentTimeMillis() * ONE_MS_IN_NANOSECONDS + this.getUniqueNumberForTheSamplerThread());

        this.setupWriter(this.resultContext);

        this.parseSamplers(context);

        this.scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        this.scheduler.setRemoveOnCancelPolicy(true);
        this.scheduler.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);

        // Indicates whether to write sub sample records to the database
        this.recordSubSamples = Boolean.parseBoolean(context.getParameter(KEY_RECORD_SUB_SAMPLES, "false"));
    }

    /**
     * Executes actions after test ending, writes the metrics before closing.
     * @param context represents {@link BackendListenerContext}.
     * @throws Exception in case of the errors.
     */
    @Override
    public void teardownTest(BackendListenerContext context) throws Exception {

        // update the context
        this.resultContext.setBackendListenerContext(context);
        this.resultContext.setTimeToSet(System.currentTimeMillis() * ONE_MS_IN_NANOSECONDS + this.getUniqueNumberForTheSamplerThread());

        this.timer.cancel();
        this.scheduler.shutdown();

        try {
            this.scheduler.awaitTermination(30, TimeUnit.SECONDS);
            LOGGER.info("Writer scheduler terminated!");
        } catch (InterruptedException e) {
            LOGGER.error("Error waiting for end of scheduler " + e);
        }

        Writer.getInstance(this.resultContext, LOGGER).close();

        this.samplersToFilter.clear();
        super.teardownTest(context);
    }


    /**
     * Empty implementation, just inherits the current interface {@link Runnable}, no user metrics required to write, like test start/end time
     * amount of user etc, added since it's a contract to implement JMeter listener.
     */
    @Override
    public void run() {
        // no user metrics needs to write
    }

    /**
     * Writes the data from the Sampler result, uses the {@link Arguments} set on UI to write data.
     * @param sampleResults represents {@link List<SampleResult>} got while the test execution.
     * @param context the {@link BackendListenerContext}.
     */
    @Override
    public void handleSampleResults(List<SampleResult> sampleResults, BackendListenerContext context) {

        this.resultContext.setBackendListenerContext(context);


        // Gather all the listeners
        List<SampleResult> allSampleResults = new ArrayList<>();
        for (SampleResult sampleResult : sampleResults) {
            allSampleResults.add(sampleResult);

            if (this.recordSubSamples) {
                Collections.addAll(allSampleResults, sampleResult.getSubResults());
            }
        }

        for (SampleResult sampleResult : allSampleResults) {
            getUserMetrics().add(sampleResult);

            if ((null != this.regexForSamplerList && sampleResult.getSampleLabel().matches(this.regexForSamplerList))
                    || this.samplersToFilter.contains(sampleResult.getSampleLabel())) {

                // Set sampler result
                this.resultContext.setSampleResult(sampleResult);
                this.resultContext.setTimeToSet(System.currentTimeMillis() * ONE_MS_IN_NANOSECONDS + this.getUniqueNumberForTheSamplerThread());

                Result result = new ResultCreator(this.resultContext).createResult();
                Result resultToWrite = ResultController.process(this.resultContext, result);

                if (resultToWrite != null) {

                    String resultRow = ResultRow.getRow(this.resultContext, result);
                    Writer.getInstance(this.resultContext, LOGGER).collectData(resultRow);
                }
            }
        }
    }

    /**
     * Setups {@link Writer}, starting the timer to import data by timer, the interval set in {@link Arguments}
     * @param context the {@link ResultContext}.
     */
    private void setupWriter(ResultContext context) {

        // Create the file and write the header
        Writer.getInstance(context, LOGGER).setupClient();

        // Write Data if any by timer
        this.importDataByTimer(context);
    }

    /**
     * Writes the data by timer.
     * @param context the {@link ResultContext}.
     */
    private void importDataByTimer(ResultContext context) {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {

                LOGGER.debug("Running the timer: " + new java.util.Date());
                Writer.getInstance(context, CsvBackendListenerClient.LOGGER).writeData();

            }
        }, 0, context.getBackendListenerContext().getIntParameter(KEY_FLUSH_INTERVAL, 4000));
    }

    /**
     * Parses the Samplers using data user sets in UI, used {@link Arguments} set before the test start.
     * @param context the {@link BackendListenerContext}.
     */
    private void parseSamplers(BackendListenerContext context) {

        //List of samplers to record.
        String samplersList = context.getParameter(KEY_SAMPLERS_LIST, "");
        this.samplersToFilter = new HashSet<>();

        if (context.getBooleanParameter(KEY_USE_REGEX_FOR_SAMPLER_LIST, false)) {
            this.regexForSamplerList = samplersList;
        } else {
            this.regexForSamplerList = null;
            String[] samplers = samplersList.split(context.getParameter(KEY_SEPARATOR));

            this.samplersToFilter = new HashSet<>();
            Collections.addAll(this.samplersToFilter, samplers);
        }
    }

    /**
     * Try to get a unique number for the sampler thread.
     */
    private int getUniqueNumberForTheSamplerThread() {

        return randomNumberGenerator.nextInt(ONE_MS_IN_NANOSECONDS);
    }
}
