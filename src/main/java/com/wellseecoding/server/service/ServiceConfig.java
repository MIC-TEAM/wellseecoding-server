package com.wellseecoding.server.service;

import com.google.common.hash.Hashing;
import com.wellseecoding.server.user.UserRepository;
import com.wellseecoding.server.user.education.EducationRepository;
import com.wellseecoding.server.user.link.LinkRepository;
import com.wellseecoding.server.user.sns.SnsInfoRepository;
import com.wellseecoding.server.user.work.WorkRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService userService(UserRepository userRepository,
                                   EducationRepository educationRepository,
                                   LinkRepository linkRepository,
                                   WorkRepository workRepository,
                                   SnsInfoRepository snsInfoRepository) {
        return new UserService(
                userRepository,
                educationRepository,
                linkRepository,
                workRepository,
                snsInfoRepository,
                Hashing.sha512(), () -> UUID.randomUUID().toString()
        );
    }
}
