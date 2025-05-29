package com.chatApp.ChatApp.service;


import com.chatApp.ChatApp.common.Dto.MessageResponse;
import com.chatApp.ChatApp.common.Dto.SendMessageRequest;
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

    public MessageResponse saveMessage(SendMessageRequest request) {
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

        return new MessageResponse(
                savedMessage.getConversation().getId(),
                savedMessage.getSenderId(),
                savedMessage.getSenderName(),
                savedMessage.getContent()
        );
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
