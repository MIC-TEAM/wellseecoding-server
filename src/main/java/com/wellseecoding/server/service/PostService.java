package com.wellseecoding.server.service;

import com.wellseecoding.server.http.handler.post.PostRequest;
import com.wellseecoding.server.post.Post;
import com.wellseecoding.server.post.PostRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public CompletableFuture<Void> write(Long userId, PostRequest postRequest) {
        return CompletableFuture.supplyAsync(() -> {
            postRepository.save(Post.builder()
                                    .userId(userId)
                                    .name(postRequest.getName())
                                    .deadline(postRequest.getDeadline())
                                    .schedule(postRequest.getSchedule())
                                    .summary(postRequest.getSummary())
                                    .qualification(postRequest.getQualification())
                                    .size(postRequest.getSize())
                                    .build());
            return null;
        });
    }

    public CompletableFuture<List<Post>> findAll() {
        return CompletableFuture.supplyAsync(() -> postRepository.findAll());
    }

    public CompletableFuture<Post> find(Long postId) {
        return CompletableFuture.supplyAsync(() -> postRepository.findById(postId))
                                .thenApply(optionalPost -> optionalPost.orElse(null));
    }

    public CompletableFuture<Void> delete(Long postId, Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            Post post = postRepository.findById(postId).orElse(null);
            if (Objects.isNull(post)) {
                throw new IllegalArgumentException("post " + postId + " does not exist");
            }
            if (ObjectUtils.notEqual(post.getUserId(), userId)) {
                throw new IllegalArgumentException("post " + postId + " does not belong to user " + userId);
            }
            postRepository.delete(post);
            return null;
        });
    }

    public CompletableFuture<Void> overwrite(Long postId, Long userId, PostRequest postRequest) {
        return CompletableFuture.supplyAsync(() -> {
            Post post = postRepository.findById(postId).orElse(null);
            if (Objects.isNull(post)) {
                throw new IllegalArgumentException("post " + postId + " does not exist");
            }
            if (ObjectUtils.notEqual(post.getUserId(), userId)) {
                throw new IllegalArgumentException("post " + postId + " does not belong to user " + userId);
            }
            post.setDeadline(postRequest.getDeadline());
            post.setName(postRequest.getName());
            post.setQualification(postRequest.getQualification());
            post.setSchedule(postRequest.getSchedule());
            post.setSize(postRequest.getSize());
            post.setSummary(postRequest.getSummary());
            postRepository.save(post);
            return null;
        });
    }
}
