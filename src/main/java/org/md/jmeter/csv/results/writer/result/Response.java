package org.md.jmeter.csv.results.writer.result;

/**
 * Represents the data transfer object of the HTTP {@link Response}.
 * @author Michail Derevyanko.
 */
public class Response {

    /**
     * Sets the local fields.
     */
    private static final String defaultValue = "";

    private String code = defaultValue;
    private String headers = defaultValue;
    private String body = defaultValue;
    private String assertionError = defaultValue;
    private long elapsedTime = 0;

    /**
     * Gets the response code.
     * @return the response code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the response code.
     * @param code the response code.
     */
    public void setCode(String code) {
        if (code != null) {
            this.code = code.trim();
        }
    }

    /**
     * Gets the response headers.
     * @return the response headers.
     */
    public String getHeaders() {
        return this.headers;
    }

    /**
     * Sets the response headers.
     * @param headers the response headers.
     */
    public void setHeaders(String headers) {
        if (headers != null) {
            this.headers = headers.trim();
        }
    }

    /**
     * Gets the response body.
     * @return response body.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Sets the response body.
     * @param body returns the response body.
     */
    public void setBody(String body) {
        if (body != null) {
            this.body = body.trim();
        }
    }

    /**
     * Gets the assertion error message in case of error.
     * @return theassertion error message in case of error.
     */
    public String getAssertionError() {
        return this.assertionError;
    }

    /**
     * Sets the assertion error message in case of error.
     * @param error assertion error message in case of error.
     */
    public void setAssertionError(String error) {
        if(error != null){
            this.assertionError = error.trim();
        }
    }

    /**
     * Gets the elapsed time - the response time.
     * @return the response time.
     */
    public long getElapsedTime() {
        return this.elapsedTime;
    }

    /**
     * Sets the response time.
     * @param elapsedTime the response time.
     */
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
