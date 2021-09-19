package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.tag.Tag;
import com.wellseecoding.server.entity.tag.TagPostMap;
import com.wellseecoding.server.entity.tag.TagPostMapRepository;
import com.wellseecoding.server.entity.tag.TagRepository;
import com.wellseecoding.server.http.handler.post.PostRequest;
import com.wellseecoding.server.entity.post.Post;
import com.wellseecoding.server.entity.post.PostRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagPostMapRepository tagPostMapRepository;

    public CompletableFuture<Void> write(Long userId, PostRequest postRequest) {
        return CompletableFuture.supplyAsync(() -> {
            Post post = postRepository.save(Post.builder()
                                                .userId(userId)
                                                .name(postRequest.getName())
                                                .deadline(postRequest.getDeadline())
                                                .schedule(postRequest.getSchedule())
                                                .summary(postRequest.getSummary())
                                                .qualification(postRequest.getQualification())
                                                .size(postRequest.getSize())
                                                .build());

            replaceTagsForPost(post.getId(), postRequest.getTags());
            return null;
        });
    }

    private void replaceTagsForPost(long postId, List<String> tagValues) {
        List<Tag> tags = new ArrayList<>();
        tagValues.forEach(tagValue -> {
            Optional<Tag> optionalTag = tagRepository.findByValue(tagValue);
            if (optionalTag.isEmpty()) {
                optionalTag = Optional.of(tagRepository.save(Tag.builder()
                                                                .value(tagValue)
                                                                .build()));
            }
            tags.add(optionalTag.get());
        });

        tags.forEach(tag -> {
            tagPostMapRepository.save(TagPostMap.builder()
                                                .tag(tag)
                                                .postId(postId)
                                                .build());
        });
    }

    public CompletableFuture<List<com.wellseecoding.server.service.model.Post>> findAll() {
        return CompletableFuture.supplyAsync(() -> postRepository.findAll()
                                                                 .stream()
                                                                 .map(com.wellseecoding.server.service.model.Post::fromEntity)
                                                                 .collect(Collectors.toList()));
    }

    public CompletableFuture<com.wellseecoding.server.service.model.Post> find(Long postId) {
        return CompletableFuture.supplyAsync(() -> postRepository.findById(postId))
                                .thenApply(optionalPost -> {
                                    if (optionalPost.isPresent()) {
                                        return com.wellseecoding.server.service.model.Post.fromEntity(optionalPost.get());
                                    }
                                    return null;
                                });
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

            tagPostMapRepository.findAllByPostId(postId)
                                .forEach(tagPostMapRepository::delete);

            replaceTagsForPost(postId, postRequest.getTags());
            return null;
        });
    }
}
