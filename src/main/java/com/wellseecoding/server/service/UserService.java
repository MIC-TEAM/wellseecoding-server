package com.wellseecoding.server.service;

import com.google.common.hash.HashFunction;
import com.wellseecoding.server.user.User;
import com.wellseecoding.server.user.UserRepository;
import com.wellseecoding.server.user.sns.SnsInfo;
import com.wellseecoding.server.user.sns.SnsInfoKey;
import com.wellseecoding.server.user.sns.SnsInfoRepository;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SnsInfoRepository snsInfoRepository;
    private final HashFunction passwordHashFunction;
    private final Supplier<String> randomStringGenerator;

    public CompletableFuture<User> createUser(String username, String password, String email) {
        return createUser(User.builder()
                              .username(username)
                              .password(passwordHashFunction.hashString(password, StandardCharsets.UTF_8).toString())
                              .email(email)
                              .refreshToken(randomStringGenerator.get())
                              .build());
    }

    private CompletableFuture<User> createUser(String username, String email) {
        return createUser(User.builder()
                              .username(username)
                              .email(email)
                              .refreshToken(randomStringGenerator.get())
                              .build());
    }

    private CompletableFuture<User> createUser(User user) {
        return CompletableFuture.supplyAsync(() -> userRepository.save(user));
    }

    private CompletableFuture<Optional<User>> getUserByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByEmail(email));
    }

    public CompletableFuture<SnsInfo> mapSnsUser(String snsUserId, String snsType, String username, String email) {
        return getSnsUser(snsUserId, snsType)
                .thenCompose(optionalSnsInfo -> {
                    if (optionalSnsInfo.isPresent()) {
                        return CompletableFuture.completedFuture(optionalSnsInfo.get());
                    }

                    return mapSnsUserToExistingUser(snsUserId, snsType, email)
                            // TODO: add logging
                            .exceptionally(throwable -> null)
                            .thenCompose(snsInfo -> {
                                if (nonNull(snsInfo)) {
                                    return CompletableFuture.completedFuture(snsInfo);
                                }

                                return mapSnsUserToNewUser(snsUserId, snsType, username, email);
                            });
                });
    }

    private CompletableFuture<Optional<SnsInfo>> getSnsUser(String snsUserId, String snsType) {
        return CompletableFuture.supplyAsync(() -> snsInfoRepository.findById(SnsInfoKey.builder()
                                                                                       .snsId(snsUserId)
                                                                                       .snsType(snsType)
                                                                                       .build()));
    }

    private CompletableFuture<SnsInfo> mapSnsUserToExistingUser(String snsUserId, String snsType, String email) {
        return getUserByEmail(email)
                .thenCompose(optionalUser -> {
                    if (optionalUser.isEmpty()) {
                        throw new IllegalStateException("user with " + email + " is not found");
                    }

                    return createSnsUser(optionalUser.get(), snsUserId, snsType);
                });
    }

    private CompletableFuture<SnsInfo> mapSnsUserToNewUser(String snsUserId, String snsType, String username, String email) {
        return createUser(username, email)
                .thenCompose(user -> createSnsUser(user, snsUserId, snsType));
    }

    private CompletableFuture<SnsInfo> createSnsUser(User user, String snsUserId, String snsType) {
        return CompletableFuture.supplyAsync(() -> snsInfoRepository.save(SnsInfo.builder()
                                                                                 .user(user)
                                                                                 .key(SnsInfoKey.builder()
                                                                                                .snsId(snsUserId)
                                                                                                .snsType(snsType)
                                                                                                .build())
                                                                                 .build()));
    }
}
