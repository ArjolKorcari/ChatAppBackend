package com.chatApp.ChatApp.service;


import com.chatApp.ChatApp.common.Dto.MessageResponse;
import com.chatApp.ChatApp.common.Dto.SendMessageRequest;
//import com.chatApp.ChatApp.common.kafka.KafkaMessageSender;
import com.chatApp.ChatApp.common.mqtt.MqttClientService;
import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.models.Message;
import com.chatApp.ChatApp.models.User;
import com.chatApp.ChatApp.repository.ConversationRepository;
import com.chatApp.ChatApp.repository.MessageRepository;
import com.chatApp.ChatApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    private final UserRepository userRepository;

    private final MqttClientService mqttClientService;
//    private final KafkaMessageSender kafkaMessageSender;

    public MessageResponse sendMessage(SendMessageRequest request) {
        Conversation conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow();
        Long senderId = Long.valueOf(request.senderId());

        User user = userRepository.findById(senderId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + senderId));



        System.out.println("User id :"+user.getId());
        System.out.println("User name: "+user.getFirstName());
        System.out.println("Last name: "+user.getLastName());
        Message message = Message.builder()
                .id(request.id())
                .conversation(conversation)
                .senderId(request.senderId())
                .senderName(user.getFirstName())
                .content(request.content())
                .timestamp(Instant.now())
                .build();

        Message savedMessage = messageRepository.save(message);

        MessageResponse messageToShow= new MessageResponse(
                savedMessage.getId(),
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
                        message.getId(),
                        message.getConversation().getId(),
                        message.getSenderId(),
                        message.getSenderName(),
                        message.getContent()
                ))
                .toList();
    }
}
