package com.hafia.ericsson.kafkaconnect.nodealarms;

import com.hafia.ericsson.kafkaconnect.nodealarms.validators.PortValidator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import java.util.Map;


public class AlarmSourceConnectorConfig extends AbstractConfig {

  //kafka required configs
  public static final String TOPIC_CONFIG = "topic";
  private static final String TOPIC_DOC = "Topic to write to";

  //Ericsson node configs
  public static final String NODE_HOST_CONFIG = "node.host";
  private static final String NODE_HOST_DOC = "Node IP/Host to authenticate SSH calls";

  public static final String NODE_PORT_CONFIG = "node.port";
  private static final String NODE_PORT_DOC = "Node SSH/port. Default=22";

  public static final String NODE_AUTH_USERNAME_CONFIG = "node.auth.username";
  private static final String NODE_AUTH_USERNAME_DOC = "Username to authenticate calls";

  public static final String NODE_AUTH_PASSWORD_CONFIG = "node.auth.password";
  private static final String NODE_AUTH_PASSWORD_DOC = "Password to authenticate calls";


  public final String topicConfig;
  public final String nodeHostConfig;
  public final int nodePortConfig;
  public final String nodeAuthUsernameConfig;
  public final String nodeAuthPasswordConfig;

  public AlarmSourceConnectorConfig(Map<?, ?> originals) {
    super(config(), originals);

    this.topicConfig = this.getString(TOPIC_CONFIG);
    this.nodeHostConfig = this.getString(NODE_HOST_CONFIG);
    this.nodePortConfig = this.getInt(NODE_PORT_CONFIG);
    this.nodeAuthUsernameConfig = this.getString(NODE_AUTH_USERNAME_CONFIG);
    this.nodeAuthPasswordConfig = this.getPassword(NODE_AUTH_PASSWORD_CONFIG).value();
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(ConfigKeyBuilder.of(TOPIC_CONFIG, Type.STRING).documentation(TOPIC_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(NODE_HOST_CONFIG, Type.STRING).documentation(NODE_HOST_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(NODE_PORT_CONFIG, Type.INT).documentation(NODE_PORT_DOC).importance(Importance.HIGH).defaultValue(22).validator(new PortValidator()).build())
        .define(ConfigKeyBuilder.of(NODE_AUTH_USERNAME_CONFIG, Type.STRING).documentation(NODE_AUTH_USERNAME_DOC).importance(Importance.HIGH).build())
        .define(ConfigKeyBuilder.of(NODE_AUTH_PASSWORD_CONFIG, Type.PASSWORD).documentation(NODE_AUTH_PASSWORD_DOC).importance(Importance.HIGH).build())
        ;
  }
}
