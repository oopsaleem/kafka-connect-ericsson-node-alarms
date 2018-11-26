package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.hafia.ericsson.kafkaconnect.nodealarms.models.OssAlarmFile;
import org.apache.kafka.connect.source.SourceRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmFileSchemas.*;


class OssAlarmFileAPISftpClientTest {

    @Test
    void test(){

    }

//    @Test
//    void getNextFile() {
//        AlarmSourceConnectorConfig config = new AlarmSourceConnectorConfig(Static.initialConfig());
//        OssAlarmFileAPISftpClient client = new OssAlarmFileAPISftpClient(config);
//
//        JSONArray alarmFiles = client.getNextFile(ZonedDateTime.now().minusMinutes(config.sinceConfig).toInstant());
//        assert  (client.getErrorMessages().size() == 0);
//        assert  (alarmFiles.length() > 0 );
//        assert  (alarmFiles.getJSONObject(0).get(ID_FIELD) instanceof Long);
//        assert  (alarmFiles.getJSONObject(0).get(FILE_ROWS_AFFECTED_FIELD) instanceof Long);
//
//        for (Object obj : alarmFiles) {
//            OssAlarmFile ossAlarmFile = OssAlarmFile.fromJson((JSONObject) obj);
//            break;
//        }
//    }
}