package com.chatApp.ChatApp.common.Dto;

public record MessageResponse(
        String conversationId,
        String senderId,
        String senderName,
        String content
) {
    @Override
    public String toString() {
        return String.format("""
            {
                "conversationId": "%s",
                "senderId": "%s",
                "senderName": "%s",
                "content": "%s"
            }""", conversationId, senderId, senderName, content);
    }
}
