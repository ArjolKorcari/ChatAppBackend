package com.chatApp.ChatApp.service;


import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.models.User;
import com.chatApp.ChatApp.repository.ConversationRepository;
import com.chatApp.ChatApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    private  final UserRepository userRepository;


    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public List<Conversation> getConversationById(String id) {
        return conversationRepository.findBySenderIdOrUserId(id,id);
    }

    public Conversation createConversation(Conversation conversation) {
        Long senderId = Long.valueOf(conversation.getSenderId());
        Long userId= Long.valueOf(conversation.getUserId());
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + senderId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        System.out.println("Id  "+user.getId());
        System.out.println("Username  "+user.getFirstName());
        conversation.setId(""+UUID.randomUUID());
        conversation.setUserName(user.getFirstName());
        conversation.setSenderName(sender.getFirstName());
        return conversationRepository.save(conversation);
    }

    public void deleteConversation(String id) {
        conversationRepository.deleteById(id);
    }
}
