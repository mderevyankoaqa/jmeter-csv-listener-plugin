package io.github.mderevyankoaqa.csv.results.writer.csv;

/**
 * Stores to CSV row header.
 * @author Michail Derevyanko.
 */
public class Header {

    /**
     * Gets the header.
     * @param separator the separator set from JMeter UI.
     * @return the header ready to write.
     */
    public static String getHeader(String separator)
    {
        return  "time" + separator
                + "requestName" + separator
                + "requestUrl" + separator
                + "requestHeaders" + separator
                + "requestData" +separator

                + "responseCode" + separator
                + "responseTime" + separator
                + "responseHeaders" + separator
                + "responseBody" + separator
                + "responseAssertionError" + separator
                + "isSuccessful";
    }
}
