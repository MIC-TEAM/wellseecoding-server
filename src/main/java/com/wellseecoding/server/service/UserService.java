package com.wellseecoding.server.service;

import com.google.common.hash.HashFunction;
import com.wellseecoding.server.http.handler.user.profile.model.Education;
import com.wellseecoding.server.http.handler.user.profile.model.Link;
import com.wellseecoding.server.http.handler.user.profile.model.Work;
import com.wellseecoding.server.entity.user.User;
import com.wellseecoding.server.entity.user.UserRepository;
import com.wellseecoding.server.entity.education.EducationRepository;
import com.wellseecoding.server.entity.link.LinkRepository;
import com.wellseecoding.server.entity.sns.SnsInfo;
import com.wellseecoding.server.entity.sns.SnsInfoKey;
import com.wellseecoding.server.entity.sns.SnsInfoRepository;
import com.wellseecoding.server.entity.work.WorkRepository;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final LinkRepository linkRepository;
    private final WorkRepository workRepository;
    private final SnsInfoRepository snsInfoRepository;
    private final HashFunction passwordHashFunction;
    private final Supplier<String> randomStringGenerator;

    public CompletableFuture<User> getUser(long userId) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(userId)).thenApply(optionalUser -> optionalUser.get());
    }

    public CompletableFuture<Void> setStatus(long userId, String status) {
        return getUser(userId)
                .thenAccept(user -> {
                    user.setStatus(status);
                    userRepository.save(user);
                });
    }

    public CompletableFuture<Void> setAboutMe(long userId, String aboutMe) {
        return getUser(userId)
                .thenAccept(user -> {
                    user.setAboutMe(aboutMe);
                    userRepository.save(user);
                });
    }

    public CompletableFuture<Void> setEducations(long userId, List<Education> educations) {
        return CompletableFuture.supplyAsync(() -> {
            educationRepository.findByUserId(userId).forEach(educationRepository::delete);
            for (Education education : educations) {
                com.wellseecoding.server.entity.education.Education educationEntity = new com.wellseecoding.server.entity.education.Education();
                educationEntity.setUserId(userId);
                educationEntity.setDegree(education.getDegree());
                educationEntity.setMajor(education.getMajor());
                educationEntity.setGraduated(education.isGraduated());
                educationRepository.save(educationEntity);
            }
            return null;
        });
    }

    public CompletableFuture<Void> setLinks(long userId, List<Link> links) {
        return CompletableFuture.supplyAsync(() -> {
            linkRepository.findByUserId(userId).forEach(linkRepository::delete);
            for (Link link : links) {
                com.wellseecoding.server.entity.link.Link linkEntity = new com.wellseecoding.server.entity.link.Link();
                linkEntity.setUserId(userId);
                linkEntity.setName(link.getName());
                linkEntity.setLink(link.getLink());
                linkEntity.setDescription(link.getDescription());
                linkRepository.save(linkEntity);
            }
            return null;
        });
    }

    public CompletableFuture<Void> setWorks(long userId, List<Work> works) {
        return CompletableFuture.supplyAsync(() -> {
            workRepository.findByUserId(userId).forEach(workRepository::delete);
            for (Work work : works) {
                com.wellseecoding.server.entity.work.Work workEntity = new com.wellseecoding.server.entity.work.Work();
                workEntity.setUserId(userId);
                workEntity.setRole(work.getRole());
                workEntity.setTechnology(work.getTechnology());
                workEntity.setYears(work.getYears());
                workRepository.save(workEntity);
            }
            return null;
        });
    }

    public CompletableFuture<User> login(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            final String hashedPassword = passwordHashFunction.hashString(password, StandardCharsets.UTF_8).toString();
            Optional<User> user = userRepository.findByEmailAndPassword(email, hashedPassword);
            if (user.isEmpty()) {
                throw new UserNotFoundException(email + " is not found");
            }
            return user.get();
        });
    }

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
