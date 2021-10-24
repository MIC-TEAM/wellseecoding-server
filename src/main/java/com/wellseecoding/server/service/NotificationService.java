package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.notification.NotificationEntity;
import com.wellseecoding.server.entity.notification.NotificationRepository;
import com.wellseecoding.server.service.model.EventCategory;
import com.wellseecoding.server.service.model.Notification;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public CompletableFuture<List<Notification>> getAllNotifications(long userId) {
        return CompletableFuture.supplyAsync(() -> {
            if (userId <= 0) {
                throw new IllegalArgumentException(userId + " is a wrong user id");
            }
            return notificationRepository.findAllByUserId(userId)
                                         .stream()
                                         .map(Notification::valueOf)
                                         .collect(Collectors.toList());
        });
    }

    public CompletableFuture<Void> notify(long userId, long postId, EventCategory eventCategory) {
        final long timestamp = System.currentTimeMillis() / 1000;
        return CompletableFuture.supplyAsync(() -> {
            if (userId <= 0) {
                throw new IllegalArgumentException(userId + " is a wrong user id");
            }
            if (postId <= 0) {
                throw new IllegalArgumentException(postId + " is a wrong post id");
            }
            notificationRepository.save(NotificationEntity.builder()
                                                          .userId(userId)
                                                          .postId(postId)
                                                          .eventCategory(eventCategory.getValue())
                                                          .timestamp(timestamp)
                                                          .stale(false)
                                                          .build());
            return null;
        });
    }
}
