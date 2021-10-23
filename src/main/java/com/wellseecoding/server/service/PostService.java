package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.comment.CommentRepository;
import com.wellseecoding.server.entity.likes.LikeRepository;
import com.wellseecoding.server.entity.post.KeywordPostMap;
import com.wellseecoding.server.entity.post.KeywordPostMapRepository;
import com.wellseecoding.server.entity.tag.Tag;
import com.wellseecoding.server.entity.tag.TagPostMap;
import com.wellseecoding.server.entity.tag.TagPostMapRepository;
import com.wellseecoding.server.entity.tag.TagRepository;
import com.wellseecoding.server.http.handler.post.PostRequest;
import com.wellseecoding.server.entity.post.Post;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.service.model.ThemedPost;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagPostMapRepository tagPostMapRepository;
    private final KeywordPostMapRepository keywordPostMapRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

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

            replaceTagsForPost(post, postRequest.getTags());
            remapKeywordAndPost(post, postRequest.getTags());
            return null;
        });
    }

    private void replaceTagsForPost(Post post, List<String> tagValues) {
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
                                                .post(post)
                                                .build());
        });
    }

    public CompletableFuture<List<com.wellseecoding.server.service.model.Post>> findAll() {
        return CompletableFuture.supplyAsync(() -> postRepository.findAll()
                                                                 .stream()
                                                                 .map(post -> {
                                                                     final long commentCount = commentRepository.countByPostId(post.getId());
                                                                     return com.wellseecoding.server.service.model.Post.fromEntity(post, commentCount);
                                                                 })
                                                                 .collect(Collectors.toList()));
    }

    public CompletableFuture<com.wellseecoding.server.service.model.Post> find(Long postId) {
        return CompletableFuture.supplyAsync(() -> postRepository.findById(postId))
                                .thenApply(optionalPost -> {
                                    if (optionalPost.isPresent()) {
                                        final long commentCount = commentRepository.countByPostId(optionalPost.get().getId());
                                        return com.wellseecoding.server.service.model.Post.fromEntity(optionalPost.get(), commentCount);
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
            likeRepository.findAllByLikeIdPostId(postId)
                          .forEach(likeRepository::delete);
            postRepository.delete(post);
            post.getTagPostMaps().forEach(tagPostMap -> {
                Tag tag = tagPostMap.getTag();
                if (tagPostMapRepository.countByTag(tag) > 0) {
                    return;
                }
                tagRepository.delete(tag);
            });
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

            replaceTagsForPost(post, postRequest.getTags());
            remapKeywordAndPost(post, postRequest.getTags());
            return null;
        });
    }

    public CompletableFuture<List<ThemedPost>> getRandomPosts() {
        return CompletableFuture.supplyAsync(() -> {
            List<ThemedPost> themedPosts = new ArrayList<>();
            List<Tag> randomTags = tagRepository.getRandomTags(3);
            randomTags.forEach(randomTag -> {
                List<TagPostMap> posts = tagPostMapRepository.findFirst10ByTag(randomTag);
                if (posts.isEmpty()) {
                    return;
                }
                themedPosts.add(new ThemedPost(randomTag.getValue(),
                                               posts.stream().map(tagPostMap -> {
                                                   final long commentCount = commentRepository.countByPostId(tagPostMap.getPost().getId());
                                                   return com.wellseecoding.server.service.model.Post.fromEntity(tagPostMap.getPost(), commentCount);
                                               }).collect(Collectors.toList())));
            });
            return themedPosts;
        });
    }

    private void remapKeywordAndPost(Post post, List<String> tags) {
        removeKeywordAndPostMap(post.getId());
        mapKeywordAndPost(post, tags);
    }

    private void removeKeywordAndPostMap(Long postId) {
        keywordPostMapRepository.findAllByPostId(postId)
                                .forEach(keywordPostMapRepository::delete);
    }

    private void mapKeywordAndPost(Post post, List<String> tags) {
        Set<String> keywords = new HashSet<>();
        keywords.addAll(Arrays.asList(StringUtils.split(post.getName(), " ")));
        keywords.addAll(Arrays.asList(StringUtils.split(post.getSummary(), " ")));
        keywords.addAll(tags);
        keywords.forEach(keyword -> {
            keywordPostMapRepository.save(KeywordPostMap.builder()
                                                        .keyword(keyword)
                                                        .post(post)
                                                        .build());
        });
    }

    public CompletableFuture<List<com.wellseecoding.server.service.model.Post>> searchPosts(List<String> keywords) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Long, com.wellseecoding.server.service.model.Post> posts = new HashMap<>();
            keywords.forEach(keyword -> {
                keywordPostMapRepository.search(keyword)
                                        .forEach(keywordPostMap -> {
                                            final Post postEntity = keywordPostMap.getPost();
                                            final Long postId = postEntity.getId();
                                            if (posts.containsKey(postId) == false) {
                                                final long commentCount = commentRepository.countByPostId(postEntity.getId());
                                                posts.put(postId, com.wellseecoding.server.service.model.Post.fromEntity(postEntity, commentCount));
                                            }
                                        });
            });
            return new ArrayList<>(posts.values());
        });
    }
}