package com.wellseecoding.server.service;

import com.google.common.hash.Hashing;
import com.wellseecoding.server.entity.comment.CommentRepository;
import com.wellseecoding.server.entity.group.MemberRepository;
import com.wellseecoding.server.entity.likes.LikeRepository;
import com.wellseecoding.server.entity.notification.NotificationRepository;
import com.wellseecoding.server.entity.post.KeywordPostMapRepository;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.tag.TagPostMapRepository;
import com.wellseecoding.server.entity.tag.TagRepository;
import com.wellseecoding.server.entity.tag.TagUserMapRepository;
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
                                   SnsInfoRepository snsInfoRepository,
                                   LikeRepository likeRepository,
                                   TagRepository tagRepository,
                                   TagUserMapRepository tagUserMapRepository) {
        return new UserService(
                userRepository,
                educationRepository,
                linkRepository,
                workRepository,
                snsInfoRepository,
                Hashing.sha512(), () -> UUID.randomUUID().toString(),
                likeRepository,
                tagRepository,
                tagUserMapRepository
        );
    }

    @Bean
    public PostService postService(PostRepository postRepository,
                                   TagRepository tagRepository,
                                   TagPostMapRepository tagPostMapRepository,
                                   KeywordPostMapRepository keywordPostMapRepository,
                                   CommentRepository commentRepository,
                                   LikeRepository likeRepository,
                                   UserRepository userRepository) {
        return new PostService(postRepository,
                               tagRepository,
                               tagPostMapRepository,
                               keywordPostMapRepository,
                               commentRepository,
                               likeRepository,
                               userRepository);
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository,
                                         UserRepository userRepository,
                                         PostRepository postRepository,
                                         NotificationService notificationService) {
        return new CommentService(commentRepository, userRepository, postRepository, notificationService);
    }

    @Bean
    public GroupService groupService(UserRepository userRepository,
                                     MemberRepository memberRepository,
                                     PostRepository postRepository,
                                     NotificationService notificationService) {
        return new GroupService(userRepository, memberRepository, postRepository, notificationService);
    }

    @Bean
    public NotificationService notificationService(UserRepository userRepository,
                                                   PostRepository postRepository,
                                                   NotificationRepository notificationRepository) {
        return new NotificationService(userRepository, postRepository, notificationRepository);
    }
}
