# KAFKA
#### $> brew cask install java
#### $> brew install kafka
#### $> brew services start zookeeper
#### $> brew services start kafka
#### $> brew services stop kafka
#### $> brew services stop zookeeper

### Simple Producer and Consumer
###### 1. Create topic
###### $> kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 4 --topic test

### Describe topic
######$> kafka-topics --describe --zookeeper localhost:2181 --topic test

### List of topics
###### $> kafka-topics --list --zookeeper localhost:2181


### Start Producer and Consumer
###### a. Terminal 1
######## $> kafka-console-producer --broker-list localhost:9092 --topic test
###### b. Terminal 2
######## $> kafka-console-consumer --broker-list localhost:9092 --topic test
######## OR (the next command is working)
######## $> kafka-console-consumer --zookeeper localhost:2181 --topic test --from-beginning


### Alter topic, modify partition count
###### $> kafka-topics --zookeeper localhost:2181 --alter --topic test --partitions 2

### Delete a topic
###### $> kafka-topics --zookeeper localhost:2181 --delete --topic test

### List of known group of consumers
###### $> kafka-consumer-groups --new-consumer --bootstrap-server localhost:9092 --list

### View the details of a consumer group
###### $> kafka-consumer-groups --zookeeper localhost:9092 --describe --group <group name>


### Kafka Streams
###### 1.Create topic : word-count-topic
######## $> kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 4 --topic word-count-topic

### 2.Produce topic
###### $> kafka-console-producer --broker-list localhost:9092 --topic word-count-topic

### 3.Run the Kafka stream topic class
###### *Stream to word-count-topic
###### *Output to word-count-output

### 4.Take a look at the results
###### $> kafka-console-consumer --topic word-count-output --from-beginning --bootstrap-server localhost:9092 --property print.key=true
######## You can also check the original value sent in stream using simple consumer


