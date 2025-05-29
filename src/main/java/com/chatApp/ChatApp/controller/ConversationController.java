package com.chatApp.ChatApp.controller;

import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public List<Conversation> getAllConversations() {
        return conversationService.getAllConversations();
    }

    @GetMapping("/{id}")
    public List<Conversation> getConversationById(@PathVariable String id) {
        return conversationService.getConversationById(id);
    }

    @PostMapping
    public Conversation createConversation(@RequestBody Conversation conversation) {
        return conversationService.createConversation(conversation);
    }

    @DeleteMapping("/{id}")
    public void deleteConversation(@PathVariable String id) {
        conversationService.deleteConversation(id);
    }
}

