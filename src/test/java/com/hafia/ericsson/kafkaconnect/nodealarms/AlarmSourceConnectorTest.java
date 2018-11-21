package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnectorConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlarmSourceConnectorTest {

  private Map<String, String> initialConfig() {
    Map<String, String> baseProps = new HashMap<>();
    baseProps.put(TOPIC_CONFIG, "node-alarms");
    baseProps.put(NODE_HOST_CONFIG, "10.6.6.122");
    baseProps.put(NODE_PORT_CONFIG, "22");
    baseProps.put(NODE_AUTH_USERNAME_CONFIG, "foo");
    baseProps.put(NODE_AUTH_PASSWORD_CONFIG, "bar");
    return (baseProps);
  }

  @Test
  public void taskConfigsShouldReturnOneTaskConfig() {
    AlarmSourceConnector alarmSourceConnector = new AlarmSourceConnector();
    alarmSourceConnector.start(initialConfig());
    assertEquals(alarmSourceConnector.taskConfigs(1).size(),1);
    assertEquals(alarmSourceConnector.taskConfigs(10).size(),1);
  }
}
