package com.chatApp.ChatApp.common.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToKafka(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent to Kafka topic [" + topic + "]: " + message);
    }
}

