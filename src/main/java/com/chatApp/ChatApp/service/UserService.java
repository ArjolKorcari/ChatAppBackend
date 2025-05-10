package com.chatApp.ChatApp.service;

import com.chatApp.ChatApp.common.Dto.UserRegistrationRequest;
import com.chatApp.ChatApp.common.Dto.UserResponse;
import com.chatApp.ChatApp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.chatApp.ChatApp.models.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.password());


        User user = new User(
                null,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                hashedPassword // Optionally hash it
        );

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhoneNumber()
        );
    }
}
