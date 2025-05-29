package com.chatApp.ChatApp.service;


import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public List<Conversation> getConversationById(String id) {
        return conversationRepository.findBySenderId(id);
    }

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    public void deleteConversation(String id) {
        conversationRepository.deleteById(id);
    }
}
