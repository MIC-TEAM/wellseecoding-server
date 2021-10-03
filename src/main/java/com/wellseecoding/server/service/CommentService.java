package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.comment.Comment;
import com.wellseecoding.server.entity.comment.CommentRepository;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

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
            commentRepository.save(Comment.builder()
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
}
