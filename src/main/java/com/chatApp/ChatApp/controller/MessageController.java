package com.chatApp.ChatApp.controller;


import com.chatApp.ChatApp.common.Dto.MessageResponse;
import com.chatApp.ChatApp.common.Dto.SendMessageRequest;
import com.chatApp.ChatApp.common.mqtt.MqttClientService;
import com.chatApp.ChatApp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final MqttClientService mqttClientService;


    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        MessageResponse message = messageService.sendMessage(request);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversationId(conversationId));
    }
}
