package org.md.jmeter.csv.results.writer.config;

/**
 * Stores Settings to be set from JMeter UI.
 * @author Michail Derevyanko.
 */
public interface Settings {

    interface Parameters {
        String KEY_PATH = "filePath";
        String KEY_SEPARATOR = "csvSeparator";
        String KEY_SAVE_OK_SAMPLERS = "isRecordSuccessfulResults";
        String KEY_SAMPLERS_LIST = "samplersList";
        String KEY_RECORD_SUB_SAMPLES = "isRecordSubSamplers";
        String KEY_BATCH_SIZE = "batchSize";
        String KEY_USE_REGEX_FOR_SAMPLER_LIST = "isUseRegexForSamplerList";
        String KEY_FLUSH_INTERVAL = "flushInterval";
        String KEY_TIME_ZONE_ID = "timeZoneId";
        String KEY_TIME_FORMAT = "timeFormat";
    }
}
