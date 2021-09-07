package com.wellseecoding.server.service;

import com.wellseecoding.server.http.handler.post.PostRequest;
import com.wellseecoding.server.post.Post;
import com.wellseecoding.server.post.PostRepository;
import lombok.AllArgsConstructor;

import java.util.List;
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
}
