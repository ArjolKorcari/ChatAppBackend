package com.chatApp.ChatApp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow unauthenticated access to the registration endpoint
                        .requestMatchers("/register").permitAll()
                        // Allow unauthenticated access to the H2 console
                        .requestMatchers("/h2-console/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        // Disable CSRF for H2 console and API endpoints (enable in production with proper config)
                        .ignoringRequestMatchers("/h2-console/**", "/register")
                )
                .headers(headers -> headers
                        // Disable X-Frame-Options for H2 console to allow frames
                        .frameOptions().disable()
                );

        return http.build();
    }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

}
