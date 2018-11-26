package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnectorConfig.NODE_AUTH_PASSWORD_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlarmSourceConnectorTest {

  private ConfigDef configDef = AlarmSourceConnectorConfig.config();

  @Test
  void initialConfigIsValid() {
    assert (configDef.validate(Static.initialConfig())
            .stream()
            .allMatch(configValue -> configValue.errorMessages().size() == 0));
  }

  @Test
  void validatePassword() {
    Map<String, String> config = Static.initialConfig();
    ConfigValue configValue = configDef.validateAll(config).get(NODE_AUTH_PASSWORD_CONFIG);
    assert (configValue.errorMessages().size() == 0);
  }

    @Test
    public void taskConfigsShouldReturnOneTaskConfig() {
        AlarmSourceConnector alarmSrcConn = new AlarmSourceConnector();
        alarmSrcConn.start(Static.initialConfig());
        assertEquals(alarmSrcConn.taskConfigs(1).size(),1);
        assertEquals(alarmSrcConn.taskConfigs(10).size(),1);
    }
}
