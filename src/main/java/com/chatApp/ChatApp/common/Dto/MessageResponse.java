package com.chatApp.ChatApp.common.Dto;

public record MessageResponse(

        String id,
        String conversationId,
        String senderId,
        String senderName,
        String content
) {
    @Override
    public String toString() {
        return String.format("""
            {
                "id": "%s",
                "conversationId": "%s",
                "senderId": "%s",
                "senderName": "%s",
                "content": "%s"
            }""",id, conversationId, senderId, senderName, content);
    }
}
