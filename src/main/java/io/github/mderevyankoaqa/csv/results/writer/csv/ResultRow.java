package io.github.mderevyankoaqa.csv.results.writer.csv;

import io.github.mderevyankoaqa.csv.results.writer.config.Settings;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import io.github.mderevyankoaqa.csv.results.writer.result.Request;
import io.github.mderevyankoaqa.csv.results.writer.result.Response;
import io.github.mderevyankoaqa.csv.results.writer.result.Result;
import io.github.mderevyankoaqa.csv.results.writer.result.ResultContext;

import java.util.concurrent.TimeUnit;

/**
 * The logic to convert the {@link Result} to CSV row.
 * @author Michail Derevyanko.
 */
public class ResultRow {

    /**
     * Gets the CSV row.
     * @param resultContext the separator set from JMeter UI.
     * @param result the {@link Result}.
     * @return the CSV row.
     */
    public static String getRow(ResultContext resultContext, Result result)
    {
        String separator = resultContext.getBackendListenerContext().getParameter(Settings.Parameters.KEY_SEPARATOR);
        Request request = result.getRequest();
        Response response = result.getResponse();

        String dateTime = getDateTime(TimeUnit.NANOSECONDS.toMillis(result.getTime()),
                resultContext.getBackendListenerContext()) + separator;

        String requestString =
                  request.getName()  + separator
                + request.getUrl() + separator
                + request.getHeaders()  + separator
                + request.getSamplerData()  + separator;

        String responseString =
                  response.getCode() + separator
                + response.getElapsedTime() + separator
                + response.getHeaders()  + separator
                + response.getBody()  + separator
                + response.getAssertionError() + separator
                + result.isSuccessful();


        return (dateTime + requestString + responseString)
                .replaceAll("[\\r\\n]", "");
    }

    /**
     * Gets the date time.
     * @param millis the milliseconds.
     * @return the string with milliseconds to use as the event ID.
     */
    private static String getDateTime(long millis, BackendListenerContext context)
    {

        var instance = java.time.Instant.ofEpochMilli(millis);

        var zonedDateTime = java.time.ZonedDateTime
                .ofInstant(instance,java.time.ZoneId.of(context.getParameter(Settings.Parameters.KEY_TIME_ZONE_ID)));

        var formatter = java.time.format.DateTimeFormatter.ofPattern(context.getParameter(Settings.Parameters.KEY_TIME_FORMAT));

        return zonedDateTime.format(formatter);
    }
}
