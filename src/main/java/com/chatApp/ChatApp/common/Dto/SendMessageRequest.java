package com.chatApp.ChatApp.common.Dto;


public record SendMessageRequest(
        String conversationId,
        String senderId,
        String senderName,
        String content
) {}
