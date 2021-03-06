package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;

public class AlarmSinkTask extends SinkTask {

  private static Logger log = LoggerFactory.getLogger(AlarmSinkTask.class);

  AlarmSinkConnectorConfig config;
  @Override
  public void start(Map<String, String> settings) {
    this.config = new AlarmSinkConnectorConfig(settings);
    log.info("MyEricsson-TODO: Create resources like database or api connections here.");
  }

  @Override
  public void put(Collection<SinkRecord> records) {

  }

  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

  }

  @Override
  public void stop() {
    //Close resources here.
  }

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }
}
