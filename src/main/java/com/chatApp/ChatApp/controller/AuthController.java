package com.chatApp.ChatApp.controller;

import com.chatApp.ChatApp.common.Dto.LoginRequest;
import com.chatApp.ChatApp.common.Dto.LoginResponse;
import com.chatApp.ChatApp.common.Dto.UserRegistrationRequest;
import com.chatApp.ChatApp.common.Dto.UserResponse;
import com.chatApp.ChatApp.service.AuthService;
import com.chatApp.ChatApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
