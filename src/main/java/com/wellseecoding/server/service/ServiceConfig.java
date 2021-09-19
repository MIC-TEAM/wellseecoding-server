package com.wellseecoding.server.service;

import com.google.common.hash.Hashing;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.tag.TagPostMapRepository;
import com.wellseecoding.server.entity.tag.TagRepository;
import com.wellseecoding.server.entity.user.UserRepository;
import com.wellseecoding.server.entity.education.EducationRepository;
import com.wellseecoding.server.entity.link.LinkRepository;
import com.wellseecoding.server.entity.sns.SnsInfoRepository;
import com.wellseecoding.server.entity.work.WorkRepository;
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

    @Bean
    public PostService postService(PostRepository postRepository,
                                   TagRepository tagRepository,
                                   TagPostMapRepository tagPostMapRepository) {
        return new PostService(postRepository,
                               tagRepository,
                               tagPostMapRepository);
    }
}
