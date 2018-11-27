package com.hafia.ericsson.kafkaconnect.nodealarms.models;

import org.json.JSONObject;

import java.time.Instant;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.*;

public class OssAlarmFile {
    private Long id;
    private Integer generation;
    private Integer rowsAffected;
    private Integer recordSequence;
    private String alarmRecord;
    private Instant modifiedAt;

    /**
     * No args constructor for use in serialization
     */
    public OssAlarmFile() {
    }

    /**
     * @param id taken from filename
     * @param generation generation of content alarms.
     * @param rowsAffected taken from file.
     * @param recordSequence record sequence in file.
     * @param alarmRecord content of alarm records.
     * @param modifiedAt file modified time.
     */
    public OssAlarmFile(Long id, Integer generation, Integer rowsAffected, Integer recordSequence, String alarmRecord, Instant modifiedAt) {
        super();

        this.id = id;
        this.generation = generation;
        this.rowsAffected = rowsAffected;
        this.recordSequence = recordSequence;
        this.alarmRecord = alarmRecord;
        this.modifiedAt = modifiedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public OssAlarmFile withId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getGeneration() { return generation; }
    public void setGeneration(Integer generation) { this.generation = generation; }
    public OssAlarmFile withGeneration(Integer generation) {
        this.generation = generation;
        return this;
    }

    public Integer getRowsAffected() { return rowsAffected; }
    public void setRowsAffected(Integer rowsAffected) { this.rowsAffected = rowsAffected; }
    public OssAlarmFile withRowsAffected(Integer rowsAffected) {
        this.rowsAffected = rowsAffected;
        return this;
    }

    public String getAlarmRecord() { return alarmRecord; }
    public void setAlarmRecord(String alarmRecord) { this.alarmRecord = alarmRecord; }
    public OssAlarmFile withAlarmRecord(String alarmRecord) {
        this.alarmRecord = alarmRecord;
        return this;
    }

    public Integer getRecordSequence() { return recordSequence; }
    public void setRecordSequence(Integer recordSequence) { this.recordSequence = recordSequence; }
    public OssAlarmFile withRecordSequence(Integer recordSequence) {
        this.recordSequence = recordSequence;
        return this;
    }

    public Instant getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(Instant modifiedAt) { this.modifiedAt = modifiedAt; }
    public OssAlarmFile withModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public static OssAlarmFile fromJson(JSONObject jsonObject) {
        OssAlarmFile ossAlarmFile = new OssAlarmFile();
        ossAlarmFile.withId(jsonObject.getLong(ID_FIELD));
        ossAlarmFile.withGeneration(jsonObject.getInt(OSS_GENERATION_FIELD));
        ossAlarmFile.withRowsAffected(jsonObject.getInt(FILE_ROWS_AFFECTED_FIELD));
        ossAlarmFile.withRecordSequence(jsonObject.getInt(RECORD_SEQUENCE_FIELD));
        ossAlarmFile.withModifiedAt(Instant.parse(jsonObject.getString(MODIFIED_AT_FIELD)));
        ossAlarmFile.withAlarmRecord(jsonObject.getString(ALARM_RECORD_FIELD));

        return ossAlarmFile;
    }
}
