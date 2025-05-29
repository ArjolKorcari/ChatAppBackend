package com.chatApp.ChatApp.repository;


import com.chatApp.ChatApp.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    List<Conversation> findByUserId(String userId);
    List<Conversation> findBySenderId(String senderId);

}
