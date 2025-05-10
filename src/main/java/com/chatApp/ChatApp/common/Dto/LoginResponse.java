package com.chatApp.ChatApp.common.Dto;

public record LoginResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String token
) {}
