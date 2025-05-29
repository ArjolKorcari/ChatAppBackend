package com.chatApp.ChatApp.common.kafka;

import com.chatApp.ChatApp.common.Dto.MessageResponse;
import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.models.Message;
import com.chatApp.ChatApp.repository.ConversationRepository;
import com.chatApp.ChatApp.repository.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class KafkaMessageConsumer {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaMessageConsumer(MessageRepository messageRepository,
                                ConversationRepository conversationRepository,
                                ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(String json) {
        try {
            MessageResponse payload = objectMapper.readValue(json, MessageResponse.class);

            Optional<Conversation> conversationOpt = conversationRepository.findById(payload.conversationId());
            if (conversationOpt.isEmpty()) {
                System.err.println("Conversation not found: " + payload.conversationId());
                return;
            }

            Message message = Message.builder()
                    .id(UUID.randomUUID().toString())
                    .conversation(conversationOpt.get())
                    .senderId(payload.senderId())
                    .senderName(payload.senderName())
                    .content(payload.content())
                    .timestamp(Instant.now())
                    .build();

            messageRepository.save(message);
            System.out.println("Saved message from Kafka: " + message.getContent());

        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
