FROM confluentinc/cp-kafka-connect:latest

WORKDIR /kafka-connect-ericsson-node-alarms
COPY config config
COPY target target

VOLUME /kafka-connect-ericsson-node-alarms/config
VOLUME /kafka-connect-ericsson-node-alarms/offsets

CMD CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')" connect-standalone config/worker.properties config/DemoSourceConnectorExample.properties