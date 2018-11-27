package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.hafia.ericsson.kafkaconnect.nodealarms.models.OssAlarmFile;
import com.hafia.ericsson.kafkaconnect.nodealarms.utils.DateUtils;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.*;

public class AlarmSourceTask extends SourceTask {
    static final Logger log = LoggerFactory.getLogger(AlarmSourceTask.class);
    public AlarmSourceConnectorConfig config;

    private Instant nextQuerySince;
    private Integer nextRecordSequence;
    private Instant lastModifiedAt;
    private OssAlarmFileAPISftpClient ossAlarmFileAPISftpClient;

    @Override
    public String version() {
        return VersionUtil.version(this.getClass());
    }

    @Override
    public void start(Map<String, String> map) {
        config = new AlarmSourceConnectorConfig(map);
        initializeLastVariables();
        ossAlarmFileAPISftpClient = new OssAlarmFileAPISftpClient(config);
    }

    private void initializeLastVariables() {
        Map<String, Object> lastSourceOffset;
        lastSourceOffset = context.offsetStorageReader().offset(sourcePartition());
        if (lastSourceOffset == null) {
            // we haven't fetched anything yet, so we initialize as per config.
            nextQuerySince = ZonedDateTime.now().minusMinutes(config.sinceConfig).toInstant();
            nextRecordSequence = 0; //Start first record in file.
        } else {
            //we have already fetched records, just resume from lastSourceOffset.
            Object offsetModifiedAt = lastSourceOffset.get(MODIFIED_AT_FIELD);
            Object offsetRecordSequence = lastSourceOffset.get(RECORD_SEQUENCE_FIELD);
            if (offsetModifiedAt instanceof String) {
                //resume from same file to verify all records has been sent to kafka.
                nextQuerySince = Instant.parse((String) offsetModifiedAt);
            }
            if (offsetRecordSequence != null && (offsetRecordSequence instanceof String)) {
                nextRecordSequence = Integer.valueOf((String) offsetRecordSequence);
                //resume the next record.
                nextRecordSequence += 1;
            }
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        // fetch data
        final ArrayList<SourceRecord> records = new ArrayList<>();
        JSONArray fileRecords = ossAlarmFileAPISftpClient.getNextFile(nextQuerySince, nextRecordSequence);
        // we'll count how many results we get with i
        boolean fetched = false;
        for (Object obj : fileRecords) {
            OssAlarmFile ossAlarmFile = OssAlarmFile.fromJson((JSONObject) obj);
            SourceRecord sourceRecord = generateSourceRecord(ossAlarmFile);
            records.add(sourceRecord);
            fetched = true;
            lastModifiedAt = ossAlarmFile.getModifiedAt();
        }

        if (fetched) {
            nextQuerySince = lastModifiedAt.plusSeconds(1); //add 1 second to ignore lastModified file.
            //reset record sequence as file was fully fetched.
            nextRecordSequence = 0;
            log.info("Next file after 5 minutes.");
            Thread.sleep(5 * 60 * 1_000);             //query after 5 minutes. Next time it will wait 15 seconds
        } else {
            log.info("Next file after 15 seconds.");
            Thread.sleep(15 * 1_000);             //query after 15 seconds.
        }
        return records;
    }

    private SourceRecord generateSourceRecord(OssAlarmFile ossAlarmFile) {
        return new SourceRecord(
                sourcePartition(),
                sourceOffset(ossAlarmFile.getModifiedAt()),
                config.topicConfig,
                null, // partition will be inferred by the framework
                KEY_SCHEMA,
                buildRecordKey(ossAlarmFile),
                VALUE_SCHEMA,
                buildRecordValue(ossAlarmFile),
                ossAlarmFile.getModifiedAt().toEpochMilli());
    }

    @Override
    public void stop() {
        //TODO: Do whatever is required to stop your task.
    }

    private Map<String, String> sourcePartition() {
        Map<String, String> map = new HashMap<>();
        map.put(OSS_GENERATION_FIELD, String.valueOf(config.ossGenerationConfig));
        return map;
    }

    private Map<String, String> sourceOffset(Instant modifiedAt) {
        Map<String, String> map = new HashMap<>();
        map.put(MODIFIED_AT_FIELD, DateUtils.MaxInstant(modifiedAt, nextQuerySince).toString());
        map.put(RECORD_SEQUENCE_FIELD, nextRecordSequence.toString());
        return map;
    }

    private Struct buildRecordKey(OssAlarmFile ossAlarmFile) {
        // Key Schema
        Struct key = new Struct(KEY_SCHEMA)
                .put(ID_FIELD, ossAlarmFile.getId())
                .put(MODIFIED_AT_FIELD, ossAlarmFile.getModifiedAt().toEpochMilli())
                .put(OSS_GENERATION_FIELD, ossAlarmFile.getGeneration())
                .put(FILE_ROWS_AFFECTED_FIELD, ossAlarmFile.getRowsAffected());
        return key;
    }

    private Struct buildRecordValue(OssAlarmFile ossAlarmFile) {
        Struct valueStruct = new Struct(VALUE_SCHEMA)
                .put(RECORD_SEQUENCE_FIELD, ossAlarmFile.getRecordSequence())
                .put(ALARM_RECORD_FIELD, ossAlarmFile.getAlarmRecord());
        return valueStruct;
    }
}