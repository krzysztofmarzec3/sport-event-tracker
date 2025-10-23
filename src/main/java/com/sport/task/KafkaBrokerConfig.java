package com.sport.task;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

import java.util.HashMap;
import java.util.Map;

//uncomment this if you want to test it with emmbeded kafka


@Configuration
public class KafkaBrokerConfig {

    @Bean
    public EmbeddedKafkaBroker kafkaBroker() {
        Map<String, String> brokerProps = new HashMap<>();
        brokerProps.put("listeners", "PLAINTEXT://localhost:9092");
        brokerProps.put("advertised.listeners", "PLAINTEXT://localhost:9092");
        brokerProps.put("listener.security.protocol.map", "PLAINTEXT:PLAINTEXT");
        return new EmbeddedKafkaZKBroker(1)
                .kafkaPorts(9092)
                .brokerProperties(brokerProps)
                .adminTimeout(500000)
                .brokerListProperty("spring.kafka.bootstrap-servers");}

    @Bean
    public NewTopic sportsScoresTopic() {
        return TopicBuilder.name("sports-scores")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
