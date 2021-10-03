package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.comment.CommentEntity;
import com.wellseecoding.server.entity.comment.CommentRepository;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.user.User;
import com.wellseecoding.server.entity.user.UserRepository;
import com.wellseecoding.server.service.model.Comment;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CompletableFuture<Void> addComment(long userId,
                                              long postId,
                                              long parentCommentId,
                                              @NonNull String text) {
        if (userId < 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException(userId + " is illegal user id"));
        }
        if (postId < 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException(postId + " is illegal post id"));
        }
        if (parentCommentId < 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException(parentCommentId + " is illegal parent comment id"));
        }
        if (StringUtils.isBlank(text)) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("text is blank"));
        }

        final long date = System.currentTimeMillis() / 1000;
        return CompletableFuture.supplyAsync(() -> {
            if (userRepository.existsById(userId) == false) {
                throw new IllegalArgumentException("user " + userId + " does not exist");
            }
            if (postRepository.existsById(postId) == false) {
                throw new IllegalArgumentException("post " + postId + " does not exist");
            }
            boolean isParentMissing = (parentCommentId > 0) &&
                                      (commentRepository.existsById(parentCommentId) == false);
            if (isParentMissing) {
                throw new IllegalArgumentException("parent comment " + parentCommentId + " does not exist");
            }
            commentRepository.save(CommentEntity.builder()
                                                .parentId(parentCommentId)
                                                .postId(postId)
                                                .userId(userId)
                                                .date(date)
                                                .deleted(false)
                                                .text(text)
                                                .build());
            return null;
        });
    }

    public CompletableFuture<List<Comment>> getComments(long postId) {
        if (postId < 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException(postId + " is illegal post id"));
        }

        return CompletableFuture.supplyAsync(() -> {
            List<CommentEntity> commentEntities = commentRepository.findAllByPostId(postId);

            Map<Long, User> userMap = new HashMap<>();

            commentEntities.forEach(commentEntity -> {
                final long userId = commentEntity.getUserId();
                if (userMap.containsKey(userId)) {
                    return;
                }
                userMap.put(userId, userRepository.findById(userId).get());
            });

            List<Comment> comments = new ArrayList<>();

            commentEntities.stream()
                           .filter(parentEntity -> Objects.equals(parentEntity.getParentId(), 0L))
                           .forEach(parentEntity -> {
                               List<Comment> childComments = new ArrayList<>();
                               commentEntities.stream()
                                              .filter(childEntity -> Objects.equals(childEntity.getParentId(), parentEntity.getId()))
                                              .forEach(childEntity -> {
                                                  childComments.add(Comment.builder()
                                                                           .userId(childEntity.getUserId())
                                                                           .userName(userMap.get(childEntity.getUserId()).getUsername())
                                                                           .commentId(childEntity.getId())
                                                                           .commentDate(childEntity.getDate())
                                                                           .text(childEntity.getText())
                                                                           .children(Collections.emptyList())
                                                                           .build());
                                              });

                               comments.add(Comment.builder()
                                                   .userId(parentEntity.getUserId())
                                                   .userName(userMap.get(parentEntity.getUserId()).getUsername())
                                                   .commentId(parentEntity.getId())
                                                   .commentDate(parentEntity.getDate())
                                                   .text(parentEntity.getText())
                                                   .children(childComments)
                                                   .build());
                           });

            return comments;
        });
    }
}
