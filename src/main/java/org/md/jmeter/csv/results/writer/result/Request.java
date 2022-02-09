package org.md.jmeter.csv.results.writer.result;

/**
 * Represents the data transfer object of the HTTP {@link Request}.
 * @author Michail Derevyanko.
 */
public class Request {
    /**
     * Sets the local fields.
     */
    private static final String defaultValue = "";

    private String name = defaultValue;
    private String url = defaultValue;
    private String headers = defaultValue;
    private String samplerData = defaultValue;

    /**
     * Gets the request URL.
     * @return the request URL.
     */
   public String getUrl() {
        return url;
    }

    /**
     * Sets the request URL.
     * @param url the request URL.
     */
    public void setUrl(String url) {
        if (url != null) {
            this.url = url.trim();
        }
    }

    /**
     * Gets the request headers.
     * @return the request headers.
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Sets the request headers.
     * @param headers the request headers.
     */
    public void setHeaders(String headers) {
        if (headers != null) {
            this.headers = headers.trim();
        }
    }

    /**
     * Gets the JMeter Sampler name.
     * @return the JMeter Sampler name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the JMeter Sampler name.
     * @param name the JMeter Sampler name.
     */
    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        }
    }

    /**
     * Gets the request data, contains the URL and the body, etc.
     * @return the request data.
     */
    public String getSamplerData() {
        return samplerData;
    }

    /**
     * Sets the request data.
     * @param samplerData the request data.
     */
    public void setSamplerData(String samplerData) {
        if (samplerData != null) {
            this.samplerData = samplerData.trim();
        }
    }
}
