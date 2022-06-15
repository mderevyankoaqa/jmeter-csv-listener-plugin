package io.github.mderevyankoaqa.csv.results.writer.result;

/**
 * Creates the {@link Result}.
 * @author Michail Derevyanko.
 */
public class ResultCreator {

    private final ResultContext resultContext;
    private final Result result;

    /**
     * Creates the new instance of the {@link ResultCreator}.
     * @param resultContext the {@link ResultContext}.
     */
    public ResultCreator(ResultContext resultContext) {
        this.resultContext = resultContext;
        this.result = new Result();
    }

    /**
     * Creates the {@link Result}.
     * @return the new instance of the {@link Result}.
     */
    public Result createResult()
    {
        this.result.setRequest(this.getRequest());
        this.result.setResponse(this.getResponse());
        this.result.setSuccessful(this.resultContext.getSampleResult().isSuccessful());

        this.result.setTime(this.resultContext.getTimeToSet());

        return this.result;
    }

    /**
     * Gets the {@link Request}.
     * @return new instance of the {@link Request}.
     */
    private Request getRequest()
    {
        Request request = new Request();

        request.setName(this.resultContext.getSampleResult().getSampleLabel());
        request.setUrl(this.resultContext.getSampleResult().getUrlAsString());
        request.setHeaders(this.resultContext.getSampleResult().getRequestHeaders());
        request.setSamplerData(this.resultContext.getSampleResult().getSamplerData());

        return request;
    }

    /**
     * Gets {@link Response}.
     * @return new instance of the {@link Response}.
     */
    private Response getResponse()
    {
        Response response = new Response();

        response.setBody(this.getResponseBody());
        response.setCode(this.resultContext.getSampleResult().getResponseCode());
        response.setHeaders(this.resultContext.getSampleResult().getResponseHeaders());
        response.setAssertionError(this.resultContext.getSampleResult().getFirstAssertionFailureMessage());
        response.setElapsedTime(this.resultContext.getSampleResult().getTime());

        return response;
    }

    /**
     * Gets response body.
     * @return returns body of the response.
     */
    private String getResponseBody()
    {
        String body = this.resultContext.getSampleResult().getResponseDataAsString();
        if(body != null && !body.isEmpty())
        {
            return body;
        }

        return null;
    }
}
