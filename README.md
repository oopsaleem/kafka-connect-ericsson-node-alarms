```running
cd ~/kafka-connect-ericsson-node-alarms/
git pull origin
rm -rf src/test/
mvn clean package
sudo docker run -it --rm -p 2181:2181 -p 3030:3030 -p 8081:8081 \
      -p 8082:8082 -p 8083:8083 -p 9092:9092 -e ADV_HOST=127.0.0.1 \
      -e RUNTESTS=0 -e max_request_size=20000000 \
      -v ~/kafka-connect-ericsson-node-alarms/target:/connectors/EricssonNodeAlarms landoop/fast-data-dev
```
```monitoring
sudo docker run --rm -it -v "$(pwd)":/connectors/EricssonNodeAlarms --net=host landoop/fast-data-dev bash

kafka-topics --create --topic demo-alarm-source-connector-topic --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181
kafka-console-consumer --zookeeper 127.0.0.1:2181 --topic demo-alarm-source-connector-topic --from-beginning
```

# Introduction

Welcome to your new Kafka Connect connector!

# Running in development

The [docker-compose.yml](docker-compose.yml) that is included in this repository is based on the Confluent Platform Docker
images. Take a look at the [quickstart](http://docs.confluent.io/current/cp-docker-images/docs/quickstart.html#getting-started-with-docker-client)
for the Docker images. 

Your development workstation needs to be able to resolve the hostnames that are listed in the `docker-compose.yml` 
file in the root of this repository. If you are using [Docker for Mac](https://docs.docker.com/v17.12/docker-for-mac/install/)
your containers will be available at the ip address `127.0.0.1`. If you are running docker-machine
you will need to determine the ip address of the virtual machine with `docker-machine ip confluent`
to determine the ip address.

```
127.0.0.1 zookeeper
127.0.0.1 kafka
127.0.0.1 schema-registry
```


```
docker-compose up -d
```


The debug script assumes that `connect-standalone` is in the path on your local workstation. Download 
the latest version of the [Kafka](https://www.confluent.io/download/) to get started.


Start the connector with debugging enabled.
 
```
./bin/debug.sh
```

Start the connector with debugging enabled. This will wait for a debugger to attach.

```
export SUSPEND='y'
./bin/debug.sh
```