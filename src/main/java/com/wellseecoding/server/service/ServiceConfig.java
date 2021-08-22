package com.wellseecoding.server.service;

import com.google.common.hash.Hashing;
import com.wellseecoding.server.user.UserRepository;
import com.wellseecoding.server.user.sns.SnsInfoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService userService(UserRepository userRepository, SnsInfoRepository snsInfoRepository) {
        return new UserService(userRepository, snsInfoRepository, Hashing.sha512(), () -> UUID.randomUUID().toString());
    }
}
