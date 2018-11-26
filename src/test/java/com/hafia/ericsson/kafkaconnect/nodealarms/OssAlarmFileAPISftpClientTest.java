package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;


class OssAlarmFileAPISftpClientTest {

    @Test
    void getNextFile() {
        AlarmSourceConnectorConfig config = new AlarmSourceConnectorConfig(Static.initialConfig());
        OssAlarmFileAPISftpClient client = new OssAlarmFileAPISftpClient(config);

        client.getNextFile(ZonedDateTime.now().minusMinutes(config.sinceConfig).toInstant());
        assert  (client.getErrorMessages().size() == 0);
    }
}