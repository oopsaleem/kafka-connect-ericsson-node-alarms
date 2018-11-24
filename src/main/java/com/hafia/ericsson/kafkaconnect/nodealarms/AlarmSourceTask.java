package com.hafia.ericsson.kafkaconnect.nodealarms;

import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AlarmSourceTask extends SourceTask {
  static final Logger log = LoggerFactory.getLogger(AlarmSourceTask.class);

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    log.info("MyEricsson-SourceTask.start() TODO: Do things here that are required to start your task. This could be open a connection to a database, etc.");
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    log.info("MyEricsson-SourceTask.poll() TODO: Create SourceRecord objects that will be sent the kafka cluster.");
    //throw new UnsupportedOperationException("This has not been implemented.");
    Thread.sleep(5 * 1_000); //sleep for 5 seconds
    return null;
  }

  @Override
  public void stop() {
    log.info("MyEricsson-SourceTask.stop() TODO: Do whatever is required to stop your task.");
  }
}