package io.github.mderevyankoaqa.csv.results.writer.result;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;

/**
 * The data object with parameters used to create {@link Result}.
 * @author Michael Derevyanko
 */
public class ResultContext {

    private long timeToSet;
    private SampleResult sampleResult;
    private BackendListenerContext backendListenerContext;


    /**
     * Gets the {@link SampleResult}.
     * @return sample result.
     */
    public SampleResult getSampleResult() {
        return this.sampleResult;
    }

    /**
     * Sets {@link SampleResult}.
     * @param sampleResult {@link SampleResult}
     */
    public void setSampleResult(SampleResult sampleResult) {
        this.sampleResult = sampleResult;
    }

    /**
     * Gets the time frame to put it in the row.
     * @return time represented in number.
     */
    public long getTimeToSet() {
        return this.timeToSet;
    }

    /**
     * Sets time of the further row.
     * @param timeToSet the time represented in number.
     */
    public void setTimeToSet(long timeToSet) {
        this.timeToSet = timeToSet;
    }

    /**
     * Gets {@link BackendListenerContext}.
     * @return the instance of the {@link BackendListenerContext}.
     */
    public BackendListenerContext getBackendListenerContext() {
        return this.backendListenerContext;
    }

    /**
     * Sets the {@link BackendListenerContext}.
     * @param backendListenerContext the instance of the {@link BackendListenerContext}.
     */
    public void setBackendListenerContext(BackendListenerContext backendListenerContext) {
        this.backendListenerContext = backendListenerContext;
    }
}
