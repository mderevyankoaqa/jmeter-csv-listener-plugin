package io.github.mderevyankoaqa.csv.results.writer.result;

import io.github.mderevyankoaqa.csv.results.writer.config.Settings;

/**
 * Checks whether write {@link Result} or not.
 * @author Michail Derevyanko.
 */
public class ResultController {

    /**
     * Checks whether write {@link Result} or not.
     * @param resultContext the {@link ResultContext}.
     * @param result the {@link Result}.
     * @return {@link Result} depends the user settings set in UI, uses {@link org.apache.jmeter.config.Arguments} to precess. Returns 'null' depends on parameters.
     */
    public static Result process(ResultContext resultContext, Result result)
    {
        boolean saveOK = resultContext.getBackendListenerContext().getBooleanParameter(Settings.Parameters.KEY_SAVE_OK_SAMPLERS, false);
        if(saveOK && result.isSuccessful())
        {
            return result;
        }

        if(!result.isSuccessful())
        {
            return result;
        }

        return null;
    }
}
