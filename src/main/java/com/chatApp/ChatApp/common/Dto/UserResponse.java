package com.chatApp.ChatApp.common.Dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {}
