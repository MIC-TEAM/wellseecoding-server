package com.wellseecoding.server.service;

import com.google.common.hash.Hashing;
import com.wellseecoding.server.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository, Hashing.sha512(), () -> UUID.randomUUID().toString());
    }
}
