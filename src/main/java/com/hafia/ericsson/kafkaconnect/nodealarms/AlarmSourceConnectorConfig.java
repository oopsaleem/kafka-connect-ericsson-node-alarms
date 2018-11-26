package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.hafia.ericsson.kafkaconnect.nodealarms.validators.PortValidator;
import com.hafia.ericsson.kafkaconnect.nodealarms.validators.TimestampValidator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;


public class AlarmSourceConnectorConfig extends AbstractConfig {

  //kafka required configs
  public static final String TOPIC_CONFIG = "topic";
  private static final String TOPIC_DOC = "Topic to write to";

  public static final String CONNECTOR_CLASS_CONFIG = "connector.class";
  private static final String CONNECTOR_CLASS_DOC = "com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnector";

  public static final String TASKS_MAX_CONFIG = "tasks.max";
  private static final String TASKS_MAX_DOC = "Maximum tasks to run. Always will be 1.";

  //Ericsson node configs
  public static final String NODE_HOST_CONFIG = "node.host";
  private static final String NODE_HOST_DOC = "Node IP/Host to authenticate SSH calls";

  public static final String NODE_PORT_CONFIG = "node.port";
  private static final String NODE_PORT_DOC = "Node SSH/port. Default=22";

  public static final String NODE_AUTH_USERNAME_CONFIG = "node.auth.username";
  private static final String NODE_AUTH_USERNAME_DOC = "Username to authenticate calls";



  public static final String NODE_AUTH_PASSWORD_CONFIG = "node.auth.password";
  private static final String NODE_AUTH_PASSWORD_DOC = "Password to authenticate calls";

  public static final String FILE_PATHS_CONFIG = "file.paths";
  private static final String FILE_PATHS_DOC = "File paths separated by ;\n" +
          "\tex. /home/khaled/myfiles/log-2018*;/home/khaled/otherfiles/*.txt";

  public static final String SINCE_CONFIG = "since.minutes";
  private static final String SINCE_DOC = "Only files modified since the specified minutes.";
  public static final String OSS_GENERATION_CONFIG = "oss.generation";
  public static final String OSS_GENERATION_DOC = "Specify type of content bearing generation. ex. 2, 3 or 4";

  public final String topicConfig;
  public final String connectorClassConfig;
  public final int tasksMaxConfig;
  public final String nodeHostConfig;
  public final int nodePortConfig;
  public final String nodeAuthUsernameConfig;
  public final String nodeAuthPasswordConfig;
  public final String filePathsConfig;
  public final long sinceConfig;
  public final int ossGenerationConfig;


    public AlarmSourceConnectorConfig(Map<?, ?> originals) {
    super(config(), originals);

    this.topicConfig = this.getString(TOPIC_CONFIG);
    this.connectorClassConfig = this.getString(CONNECTOR_CLASS_CONFIG);
    this.tasksMaxConfig = this.getInt(TASKS_MAX_CONFIG);
    this.nodeHostConfig = this.getString(NODE_HOST_CONFIG);
    this.nodePortConfig = this.getInt(NODE_PORT_CONFIG);
    this.nodeAuthUsernameConfig = this.getString(NODE_AUTH_USERNAME_CONFIG);
    this.nodeAuthPasswordConfig = this.getPassword(NODE_AUTH_PASSWORD_CONFIG).value();
    this.filePathsConfig = this.getString(FILE_PATHS_CONFIG);
    this.sinceConfig = this.getLong(SINCE_CONFIG);
    this.ossGenerationConfig = this.getInt(OSS_GENERATION_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(ConfigKeyBuilder.of(TOPIC_CONFIG, Type.STRING).documentation(TOPIC_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(TASKS_MAX_CONFIG, Type.INT).documentation(TASKS_MAX_DOC).importance(Importance.HIGH)
                .defaultValue(1).build())
        .define(ConfigKeyBuilder.of(CONNECTOR_CLASS_CONFIG, Type.STRING).documentation(CONNECTOR_CLASS_DOC).importance(Importance.HIGH)
                .defaultValue("com.hafia.ericsson.kafkaconnect.nodealarms.AlarmSourceConnector").build())
        .define(ConfigKeyBuilder.of(NODE_HOST_CONFIG, Type.STRING).documentation(NODE_HOST_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(NODE_PORT_CONFIG, Type.INT).documentation(NODE_PORT_DOC).importance(Importance.HIGH)
                .defaultValue(22).validator(new PortValidator()).build())
        .define(ConfigKeyBuilder.of(NODE_AUTH_USERNAME_CONFIG, Type.STRING).documentation(NODE_AUTH_USERNAME_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(NODE_AUTH_PASSWORD_CONFIG, Type.PASSWORD).documentation(NODE_AUTH_PASSWORD_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(FILE_PATHS_CONFIG, Type.STRING).documentation(FILE_PATHS_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(SINCE_CONFIG, Type.LONG).documentation(SINCE_DOC).importance(Importance.HIGH)
                .defaultValue(60).build())
        .define(ConfigKeyBuilder.of(OSS_GENERATION_CONFIG, Type.INT).documentation(OSS_GENERATION_DOC).importance(Importance.HIGH)
                .defaultValue(2).build())
    ;
  }
}
