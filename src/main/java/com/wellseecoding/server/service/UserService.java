package com.wellseecoding.server.service;

import com.google.common.hash.HashFunction;
import com.wellseecoding.server.user.User;
import com.wellseecoding.server.user.UserRepository;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HashFunction passwordHashFunction;
    private final Supplier<String> randomStringGenerator;

    public CompletableFuture<User> createUser(String username, String password, String email) {
        final String hashedPassword = passwordHashFunction.hashString(password, StandardCharsets.UTF_8).toString();
        final String refreshToken = randomStringGenerator.get();
        // TODO: add a dedicated executor
        return CompletableFuture.supplyAsync(() -> {
            return userRepository.save(User.builder()
                                           .username(username)
                                           .password(hashedPassword)
                                           .email(email)
                                           .refreshToken(refreshToken)
                                           .build());
        });
    }
}
