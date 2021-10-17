package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.comment.CommentRepository;
import com.wellseecoding.server.entity.group.Member;
import com.wellseecoding.server.entity.group.MemberRepository;
import com.wellseecoding.server.entity.likes.LikeRepository;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.service.model.HomePostCollection;
import com.wellseecoding.server.service.model.Post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HomeService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public CompletableFuture<HomePostCollection> getHomePosts(long userId) {
        if (userId <= 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException(userId + " is illegal user id"));
        }
        return CompletableFuture.supplyAsync(() -> {
            return HomePostCollection.builder()
                                     .createdGroups(getCreatedPosts(userId))
                                     .registeredGroups(getRegisteredPosts(userId))
                                     .appliedGroups(getAppliedPosts(userId))
                                     .likedGroups(getLikedPosts(userId))
                                     .build();
        });
    }

    private List<Post> getCreatedPosts(long userId) {
        return postRepository.findAllByUserId(userId)
                             .stream()
                             .map(post -> {
                                 long commentCount = commentRepository.countByPostId(post.getId());
                                 return Post.fromEntity(post, commentCount);
                             })
                             .collect(Collectors.toList());
    }

    private List<Post> getRegisteredPosts(long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .filter(Member::isAuthorized)
                               .map(member -> {
                                   long commentCount = commentRepository.countByPostId(member.getPost().getId());
                                   return Post.fromEntity(member.getPost(), commentCount);
                               })
                               .collect(Collectors.toList());
    }

    private List<Post> getAppliedPosts(long userId) {
        return memberRepository.findAllByUserId(userId)
                               .stream()
                               .filter(member -> member.isAuthorized() == false)
                               .map(member -> {
                                   long commentCount = commentRepository.countByPostId(member.getPost().getId());
                                   return Post.fromEntity(member.getPost(), commentCount);
                               })
                               .collect(Collectors.toList());
    }

    private List<Post> getLikedPosts(long userId) {
        return likeRepository.findAllByLikeIdUserId(userId)
                             .stream()
                             .map(like -> {
                                 final long postId = like.getLikeId().getPostId();
                                 long commentCount = commentRepository.countByPostId(postId);
                                 return Post.fromEntity(postRepository.findById(postId).get(), commentCount);
                             })
                             .collect(Collectors.toList());
    }
}
