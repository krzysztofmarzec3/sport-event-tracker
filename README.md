# REST Calls

## Overview
Java Spring Boot microservice that:
- Receives event status updates (live ↔ not live).
- For live events, polls an external REST API every 10 seconds.
- Transforms the response and publishes to Kafka.
- Provides mock external API for local testing.
- Includes error handling, logging, and tests.

## Setup & Run
1. Prereqs: Java 17, Maven, Kafka (_localhost:9092_).
    - Quick start Kafka (Docker):
      ```bash
      docker run -p 9092:9092 -p 2181:2181 --env KAFKA_ADVERTISED_HOST_NAME=localhost \
        --env KAFKA_ZOOKEEPER_CONNECT=localhost:2181 wurstmeister/kafka
      ```
   ** _**Run notes**_ 
   - Ensure Kafka is reachable at _localhost:9092_ or update _application.yml_. 
   - Topic _sports-scores_ is used via KafkaTemplate.sendDefault; 
   create the topic if your broker doesn't auto-create:
   ```bash
   kafka-topics --bootstrap-server localhost:9092 --create --topic sports-scores --partitions 1 --replication-factor 1
   ```
2. Build & run:
   ```bash
   mvn clean package
   java -jar target/sports-tracker-0.0.1-SNAPSHOT.jar
   ```
    You can use rest client script [test-rest-api.http](src/test/resources/test-rest-api.http) to trigger events
3. Tests Run:
   ```bash
    mvn test
   ```
   Covers:
    * Status updates (validation and 200 OK).

   * Scheduling lifecycle (start/stop).

   * Kafka publication (sample embedded test; alternatively mock KafkaTemplate).
   * Application startup 

4. **Design decisions summary**

* Per-event scheduler with ThreadPoolTaskScheduler for explicit lifecycle control.

* Non-blocking WebClient with timeout and bounded retry to handle transient network issues.

* Kafka producer is idempotent, with acks=all and retries enabled; publish callbacks log success/fail.

* Mock external API included for executable prototype and integration testing.

* Payload is concise JSON with type/version for forward compatibility.

* Test containers used in tests

5. **AI-assisted parts**


Skeleton structure generated in Intelij, initial class stubs were generated with an AI assistant.

All code was reviewed, adjusted for Spring Boot 3 compatibility, improved logging, bounded retries, and input validation.

Tests and README were authored and refined manually to ensure executability.

Unit test created with AI and fixed manually incorrect method mocking