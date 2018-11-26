package com.hafia.ericsson.kafkaconnect.nodealarms;

import java.util.HashMap;
import java.util.Map;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnectorConfig.*;

public class Static {
    public static Map<String, String> initialConfig() {
        Map<String, String> baseProps = new HashMap<>();
        baseProps.put(TOPIC_CONFIG, "alarm-source-connector-topic");
        baseProps.put("tasks.max", "10");
        baseProps.put("connector.class", "com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnector");
        baseProps.put(NODE_HOST_CONFIG, "10.10.10.10");
        baseProps.put(NODE_PORT_CONFIG, "22");
        baseProps.put(NODE_AUTH_USERNAME_CONFIG, "testuser");
        baseProps.put(NODE_AUTH_PASSWORD_CONFIG, "testpasswd");
        baseProps.put(FILE_PATHS_CONFIG, "/home/ehusalo/fileDirectory/2gFiles/alarmLog/2G_alarm_out_*; " +
                "/home/ehusalo/alarmFiles/filesInserted/2GFiles/2G_alarm_out_*");
        baseProps.put(SINCE_CONFIG, "30");
        return baseProps;
    }
}
