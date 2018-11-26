package com.hafia.ericsson.kafkaconnect.nodealarms.models;

import org.json.JSONObject;

import java.time.Instant;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.*;

public class OssAlarmFile {
    private Integer id;
    private Integer generation;
    private Integer rowsAffected;
    private String fileContent;
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
     * @param fileContent content of alarm records.
     * @param modifiedAt file modified time.
     */
    public OssAlarmFile(Integer id, Integer generation, Integer rowsAffected, String fileContent, Instant modifiedAt) {
        super();

        this.id = id;
        this.generation = generation;
        this.rowsAffected = rowsAffected;
        this.fileContent = fileContent;
        this.modifiedAt = modifiedAt;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public OssAlarmFile withId(Integer id) {
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

    public String getFileContent() { return fileContent; }
    public void setFileContent(String fileContent) { this.fileContent = fileContent; }
    public OssAlarmFile withFileContent(String fileContent) {
        this.fileContent = fileContent;
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
        ossAlarmFile.withId(jsonObject.getInt(ID_FIELD));
        ossAlarmFile.withGeneration(jsonObject.getInt(OSS_GENERATION_FIELD));
        ossAlarmFile.withRowsAffected(jsonObject.getInt(FILE_ROWS_AFFECTED_FIELD));
        ossAlarmFile.withModifiedAt(Instant.parse(jsonObject.getString(MODIFIED_AT_FIELD)));
        ossAlarmFile.withFileContent(jsonObject.getString(FILE_CONTENT_FIELD));

        return ossAlarmFile;
    }
}
