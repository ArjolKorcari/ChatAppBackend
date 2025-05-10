package com.chatApp.ChatApp.service;

import com.chatApp.ChatApp.common.Dto.LoginRequest;
import com.chatApp.ChatApp.common.Dto.LoginResponse;
import com.chatApp.ChatApp.models.User;
import com.chatApp.ChatApp.repository.UserRepository;
import com.chatApp.ChatApp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        // Try to find user by email or phone number
        User user = userRepository.findByEmail(loginRequest.identifier())
                .orElseGet(() -> userRepository.findByPhoneNumber(loginRequest.identifier())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email/phone number or password")));

        System.out.println("User "+user.getFirstName()+" Founf");
        // Verify password
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email/phone number or password");
        }
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        // Generate JWT
        var token= jwtUtil.generateToken(user.getId().toString(), user.getEmail(), user.getPhoneNumber());
        System.out.println("token generated"+ token);
        return new LoginResponse(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), token);
    }
}