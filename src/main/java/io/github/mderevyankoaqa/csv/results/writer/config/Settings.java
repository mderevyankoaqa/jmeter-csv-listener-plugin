package io.github.mderevyankoaqa.csv.results.writer.config;

/**
 * Stores Settings to be set from JMeter UI.
 * @author Michail Derevyanko.
 */
public interface Settings {

    /**
     * Represents the parameters.
     */
    interface Parameters {
        /**
         * Hte file path.
         */
        String KEY_PATH = "filePath";
        /**
         * The separator in csv file.
         */
        String KEY_SEPARATOR = "csvSeparator";
        /**
         * The key used to indicate the saving of the successful  results.
         */
        String KEY_SAVE_OK_SAMPLERS = "isRecordSuccessfulResults";
        /**
         * The sampler list used while the test.
         */
        String KEY_SAMPLERS_LIST = "samplersList";
        /**
         * The key used to indicate the saving sub samplers.
         */
        String KEY_RECORD_SUB_SAMPLES = "isRecordSubSamplers";
        /**
         * The batch size.
         */
        String KEY_BATCH_SIZE = "batchSize";
        /**
         * The  key used to indicate the using regex logic.
         */
        String KEY_USE_REGEX_FOR_SAMPLER_LIST = "isUseRegexForSamplerList";
        /**
         * The flush interval.
         */
        String KEY_FLUSH_INTERVAL = "flushInterval";
        /**
         * The time zone id.
         */
        String KEY_TIME_ZONE_ID = "timeZoneId";
        /**
         * The time format.
         */
        String KEY_TIME_FORMAT = "timeFormat";
    }
}
