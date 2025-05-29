package com.chatApp.ChatApp.repository;

import com.chatApp.ChatApp.models.Conversation;
import com.chatApp.ChatApp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversation(Conversation conversation);
}
