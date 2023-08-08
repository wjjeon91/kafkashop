REM=============================start-all.bat=============================
@echo off
echo Running zookeeper-server-start.bat...
start zookeeper-server-start.bat D:\tools\kafka_2.13-3.1.2\config\zookeeper.properties
:WAIT_ZOOKEEPER
timeout /t 5 > nul
>nul 2>&1 (
    echo | "D:\tools\kafka-3.1.2-src\bin\windows\zookeeper-shell.bat" localhost:2181 ls /
)
if %errorlevel% equ 0 (
    echo ZooKeeper is up and running. Proceeding with kafka-server-start...
) else (
    echo Waiting for ZooKeeper to start...
    goto :WAIT_ZOOKEEPER
)
echo Running kafka-server-start.bat...
start kafka-server-start.bat D:\tools\kafka_2.13-3.1.2\config\server.properties
echo kafka-server-start.bat executed.

REM=============================stop-all.bat=============================
@echo off
echo Stopping Kafka...
kafka-server-stop.bat
echo Stopping ZooKeeper...
zookeeper-server-stop.bat
echo Kafka and ZooKeeper have been stopped.

REM=============================create-topicTest.bat=============================
@echo off
start kafka-topics.bat --create --bootstrap-server localhost:9092 --topic dev-topic
start kafka-topics.bat --list --bootstrap-server localhost:9092

REM=============================create-topic.bat=============================
@echo off
start kafka-topics.bat --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 1

REM=============================create-cosumerGroup.bat=============================
@echo off
start kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group my-first-application

REM=============================kafka-console-producer.bat=============================
@echo off
echo Running kafka-console-producer.bat...
start kafka-console-producer.bat --topic dev-topic --bootstrap-server localhost:9092

REM=============================kafka-console-producer1.bat=============================
@echo off
echo Running kafka-console-producer.bat...
start kafka-console-producer.bat --bootstrap-server localhost:9092 --topic first_topic

REM=============================kafka-console-consumer.bat=============================
@echo off
echo Running kafka-console-consumer.bat...
start kafka-console-consumer.bat --topic dev-topic --from-beginning --bootstrap-server localhost:9092

REM=============================kafka-console-consumer1.bat=============================
@echo off
echo Running kafka-console-consumer.bat...
start kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
start kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
start kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic first_topic --group my-first-application