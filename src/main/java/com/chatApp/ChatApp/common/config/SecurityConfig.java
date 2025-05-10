package com.chatApp.ChatApp.common.config;

import com.chatApp.ChatApp.utils.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow unauthenticated access to public endpoints
                        .requestMatchers("/register", "/auth", "/h2-console/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        // Disable CSRF for H2 console and API endpoints
                        .ignoringRequestMatchers("/h2-console/**", "/register", "/auth")
                )
                .headers(headers -> headers
                        // Disable X-Frame-Options for H2 console using non-deprecated method
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .sessionManagement(session -> session
                        // Use stateless session for JWT
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}