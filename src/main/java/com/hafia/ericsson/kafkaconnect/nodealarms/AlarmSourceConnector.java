package com.hafia.ericsson.kafkaconnect.nodealarms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationImportant;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationNote;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationTip;
import com.github.jcustenborder.kafka.connect.utils.config.Title;

@Description("This is a description of this connector and will show up in the documentation")
@DocumentationImportant("This is a important information that will show up in the documentation.")
@DocumentationTip("This is a tip that will show up in the documentation.")
@Title("Super Source Connector") //This is the display name that will show up in the documentation.
@DocumentationNote("This is a note that will show up in the documentation")
public class AlarmSourceConnector extends SourceConnector {

  private static Logger log = LoggerFactory.getLogger(AlarmSourceConnector.class);
  private AlarmSourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    config = new AlarmSourceConnectorConfig(map);

    log.info("SourceConnector.start() TODO: Add things you need to do to setup your connector.");
  }

  @Override
  public Class<? extends Task> taskClass() {
    log.info("SourceConnector.taskClass() TODO: Return your task implementation.");
    return AlarmSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int i) {
    log.info("SourceConnector.taskConfigs() TODO: Define the individual task configurations that will be executed.");

    ArrayList<Map<String, String>> configs = new ArrayList<>(1);
    configs.add(config.originalsStrings());
    return configs;
  }

  @Override
  public void stop() {
    log.info("SourceConnector.stop() TODO: Do things that are necessary to stop your connector.");
  }

  @Override
  public ConfigDef config() {
    return AlarmSourceConnectorConfig.config();
  }
}
