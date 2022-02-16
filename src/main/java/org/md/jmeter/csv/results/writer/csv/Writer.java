package org.md.jmeter.csv.results.writer.csv;

import org.md.jmeter.csv.results.writer.result.ResultContext;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.md.jmeter.csv.results.writer.config.Settings.Parameters.*;

/**
 * Represents the logic to write the {@link org.md.jmeter.csv.results.writer.result.Result} object to the file in CSV format.
 * @author Michail Derevyanko.
 */
public class Writer {
    /**
     * Sets the local fields.
     */
    private static org.slf4j.Logger LOGGER;
    private final ResultContext resultContext;
    private final List<String> results;

    private  File csvFile;
    private static volatile Writer instance;
    private boolean isHeaderWritten = false;

    /**
     * Creates the new instance of the {@link Writer}
     * @param resultContext the {@link ResultContext}, used to fetch the data set before the test execution.
     * @param logger the {@link Logger}, used to write the messages in the JMeter log file.
     */
    private Writer(ResultContext resultContext, Logger logger) {

        this.results = Collections.synchronizedList(new ArrayList<>());
        this.resultContext = resultContext;
        LOGGER = logger;

        LOGGER.info("New instance of Writer has been created!");
    }

    /**
     * Get or creates the new instance of the {@link Writer}.
     * @param resultContext the {@link ResultContext}, used to fetch the data set before the test execution.
     * @param logger the {@link Logger}, used to write the messages in the JMeter log file.
     * @return new instance of the {@link Writer}.
     */
    public static Writer getInstance(ResultContext resultContext, Logger logger) {

        Writer result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Writer.class) {
            if (instance == null) {
                instance = new Writer(resultContext, logger);
            }
            return instance;
        }
    }

    /**
     * Collects the data before writing.
     * @param row - represents CSV row with separator set.
     */
    public synchronized void collectData(String row) {

        LOGGER.debug("Collecting Data to write -> " + row);
        this.results.add(row);

        this.checkBatchSize();
    }

    /**
     * Closes the process, writes the latest collected data if any, refreshes the objects.
     */
    public synchronized void close() {

        LOGGER.info("The final step ---> writing data before closing.");
        this.writeData();

        this.results.clear();

        if (instance != null) {

            instance = null;
            LOGGER.info("Writer client has been refreshed!");
        }
    }

    /**
     * Writes the collected data.
     */
    public synchronized void writeData() {

         if (this.results.size() != 0) {
            try {

                // Write header and setup the file while the first things writing - one time.
                if (!this.isHeaderWritten)
                {
                    this.writeHeader();
                    this.isHeaderWritten = true;
                }

                String results = getString(this.results);

                long start = System.currentTimeMillis();
                this.writeStrings(Objects.requireNonNull(results));
                long end = System.currentTimeMillis();

                LOGGER.info("Data has been written successfully, batch with size is --> " + this.results.size() + ", elapsed time is --> " + (end - start) + " ms");

                this.results.clear();
                LOGGER.debug("Results have been cleaned");

            } catch (Exception e) {
                LOGGER.error("Error has occurred, batch with size " + this.results.size() + " was not imported, see the details --> " + e.getMessage());
            }
        }
    }

    /**
     * Setup the write client, sets the path to be used to write the collected data.
     */
    public synchronized void setupClient() {

        var path = this.resultContext.getBackendListenerContext().getParameter(KEY_PATH).trim();
        LOGGER.info("The Path : " + path + " is going to be used.");

        this.csvFile = new File(path);
    }

    /**
     * Writes the CSV file header, uses the separator set from the user setting - UI JMeter. Recreates the file if it exists.
     */
    private synchronized void writeHeader()
    {
        this.recreateFileIfExists(this.csvFile);

        String header = Header.getHeader(this.resultContext.getBackendListenerContext().getParameter(KEY_SEPARATOR));
        LOGGER.info("The header is : " + header);

        this.writeStrings(header);
    }

    /**
     * Checks the batch size.
     */
    private synchronized void checkBatchSize() {
            if (this.results.size() >= this.resultContext.getBackendListenerContext().getIntParameter(KEY_BATCH_SIZE)) {

            LOGGER.info("Batch size protection has occurred.");
            this.writeData();
        }
    }

    /**
     * Recreates the file on the disk if it exists.
     * @param file the instance of the {@link File} with path pointed to the new file.
     */
    private void recreateFileIfExists(File file)  {

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        }
        catch (Exception ex)
        {
            LOGGER.error(ex.toString());
        }
    }

    /**
     * Writes the string data on the disc.
     * @param data - the string to write.
     */
    private synchronized void writeStrings(String data) {

       BufferedWriter writer;
        writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(this.csvFile, true));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets the {@link String} from the collected data stored in the {@link List<String>}.
     * @param strings {@link List<String>}.
     * @return the {@link String} to write.
     */
    private String getString(List<String> strings) {
        StringBuilder theFinalString = null;
        for (String string : strings) {
            theFinalString = (theFinalString == null ? new StringBuilder() : theFinalString)
                    .append(System.lineSeparator())
                    .append(string);
        }

        return theFinalString == null ? null : theFinalString.toString();
    }
}