package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.group.Member;
import com.wellseecoding.server.entity.group.MemberRepository;
import com.wellseecoding.server.entity.post.Post;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.user.User;
import com.wellseecoding.server.entity.user.UserRepository;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CompletableFuture<List<com.wellseecoding.server.service.model.Member>> getGroupsForUser(long userId) {
        return CompletableFuture.supplyAsync(() -> stripEntity(memberRepository.findAllByUserId(userId)));
    }

    private List<com.wellseecoding.server.service.model.Member> stripEntity(Collection<Member> members) {
        return members.stream()
                      .map(member -> {
                          final Optional<User> user = userRepository.findById(member.getUserId());
                          return new com.wellseecoding.server.service.model.Member(member.getUserId(),
                                                                                   member.getPost().getId(),
                                                                                   user.get().getUsername(),
                                                                                   member.isAuthorized());
                      })
                      .collect(Collectors.toList());
    }

    public CompletableFuture<List<com.wellseecoding.server.service.model.Member>> getMembersForPost(long userId, long postId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new IllegalArgumentException(postId + " does not exist");
            }
            if (Objects.equals(userId, post.get().getUserId()) == false) {
                throw new IllegalArgumentException(userId + " is not authorized");
            }
            return stripEntity(post.get().getMembers());
        });
    }

    public CompletableFuture<Void> applyAsMember(long userId, long postId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new IllegalArgumentException(postId + " does not exist");
            }

            Optional<Member> member = getMember(post.get().getMembers(), userId);
            boolean isAuthorizedMember = member.isPresent() &&
                                         member.get().isAuthorized();
            if (isAuthorizedMember) {
                return null;
            }
            memberRepository.save(Member.builder()
                                        .userId(userId)
                                        .authorized(false)
                                        .post(post.get())
                                        .build());
            return null;
        });
    }

    private Optional<Member> getMember(Collection<Member> members, long userId) {
        for (Member member : members) {
            if (Objects.equals(userId, member.getUserId())) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    public CompletableFuture<Void> acceptApplicant(long sourceUserId,
                                                   long targetUserId,
                                                   long postId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new IllegalArgumentException(postId + " does not exist");
            }
            if (Objects.equals(sourceUserId, post.get().getUserId()) == false) {
                throw new IllegalArgumentException(sourceUserId + " is not authorized");
            }

            Optional<Member> member = getMember(post.get().getMembers(), targetUserId);
            if (member.isEmpty()) {
                throw new IllegalArgumentException(targetUserId + " is not an applicant");
            }

            member.get().setAuthorized(true);
            memberRepository.save(member.get());
            return null;
        });
    }
}
