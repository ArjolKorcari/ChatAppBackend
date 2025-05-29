package com.chatApp.ChatApp.service;


import com.chatApp.ChatApp.common.Dto.MessageResponse;
import com.chatApp.ChatApp.common.Dto.SendMessageRequest;
import com.chatApp.ChatApp.common.kafka.KafkaMessageSender;
import com.chatApp.ChatApp.common.mqtt.MqttClientService;
import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.models.Message;
import com.chatApp.ChatApp.repository.ConversationRepository;
import com.chatApp.ChatApp.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    private final MqttClientService mqttClientService;
    private final KafkaMessageSender kafkaMessageSender;

    public MessageResponse sendMessage(SendMessageRequest request) {
        Conversation conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow();

        Message message = Message.builder()
                .id("msg-" + UUID.randomUUID())
                .conversation(conversation)
                .senderId(request.senderId())
                .senderName(request.senderName())
                .content(request.content())
                .timestamp(Instant.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        MessageResponse messageToShow= new MessageResponse(
                savedMessage.getConversation().getId(),
                savedMessage.getSenderId(),
                savedMessage.getSenderName(),
                savedMessage.getContent()
        );

        System.out.println("Message string object: "+ messageToShow.toString());

        mqttClientService.publishMessage("chat/"+messageToShow.conversationId(),messageToShow.toString());




        return messageToShow;
    }



    public List<MessageResponse> getMessagesByConversationId(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        return messageRepository.findByConversation(conversation)
                .stream()
                .map(message -> new MessageResponse(
                        message.getConversation().getId(),
                        message.getSenderId(),
                        message.getSenderName(),
                        message.getContent()
                ))
                .toList();
    }
}
