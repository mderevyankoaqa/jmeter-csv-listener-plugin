package io.github.mderevyankoaqa.csv.results.writer.result;

/**
 * Represents tha data transfer object {@link Result}.
 * @author Michail Derevyanko.
 */
public class Result {

    /**
     * Sets the local fields.
     */
    private Request request;
    private Response response;
    private boolean isSuccessful;
    private long time;

    /**
     * Gets the {@link Request}
     * @return the instance of the {@link Request}.
     */
    public Request getRequest() {
        return this.request;
    }

    /**
     * Sets the {@link Result}
     * @param request the instance of the {@link Request}.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the {@link Response}.
     * @return the instance of the {@link Response}.
     */
    public Response getResponse() {
        return this.response;
    }

    /**
     * Sets the {@link Response}.
     * @param response the instance of the {@link Response}.
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * Checks whether {@link Response} has error or not.
     * @return true if the response has no error.
     */
    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    /**
     * Sets boolean flag to indicate {@link Response} has error or not.
     * @param hasErrors sets true if the response has no error.
     */
    public void setSuccessful(boolean hasErrors) {
        this.isSuccessful = hasErrors;
    }

    /**
     * Gets the time of event in UNIX format.
     * @return the time of the event.
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Sets time in the UNIX format.
     * @param time the time of the event in the UNIX format.
     */
    public void setTime(long time) {
        this.time = time;
    }
}
