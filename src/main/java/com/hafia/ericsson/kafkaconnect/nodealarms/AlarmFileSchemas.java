package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class AlarmFileSchemas {

    public static String ID_FIELD = "file_id";
    public static String OSS_GENERATION_FIELD = "oss_generation";
    public static String FILE_ROWS_AFFECTED_FIELD = "file_rows_affected";
    public static String RECORD_SEQUENCE_FIELD = "record_sequence";
    public static String ALARM_RECORD_FIELD = "alarm_record";
    public static String MODIFIED_AT_FIELD = "modified_at";

    // Schema names
    public static String SCHEMA_KEY = "ossAlarmFile_key";
    public static String SCHEMA_VALUE = "oss_alarm_file";

    // Key Schema
    public static Schema KEY_SCHEMA = SchemaBuilder.struct().name(SCHEMA_KEY)
            .version(1)
            .field(ID_FIELD, Schema.INT64_SCHEMA)
            .field(OSS_GENERATION_FIELD, Schema.INT32_SCHEMA)
            .field(FILE_ROWS_AFFECTED_FIELD, Schema.INT32_SCHEMA)
            .field(MODIFIED_AT_FIELD, Schema.INT64_SCHEMA)
            .build();

    public static Schema VALUE_SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE)
            .version(1)
            .field(RECORD_SEQUENCE_FIELD, Schema.INT32_SCHEMA)
            .field(ALARM_RECORD_FIELD, Schema.STRING_SCHEMA)
            .build();
}
