package com.wellseecoding.server.service;

import com.wellseecoding.server.entity.notification.NotificationEntity;
import com.wellseecoding.server.entity.notification.NotificationRepository;
import com.wellseecoding.server.entity.post.Post;
import com.wellseecoding.server.entity.post.PostRepository;
import com.wellseecoding.server.entity.user.User;
import com.wellseecoding.server.entity.user.UserRepository;
import com.wellseecoding.server.service.model.EventCategory;
import com.wellseecoding.server.service.model.Notification;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;

    public CompletableFuture<List<Notification>> getAllNotifications(long receiverUserId) {
        return CompletableFuture.supplyAsync(() -> {
            if (receiverUserId <= 0) {
                throw new IllegalArgumentException(receiverUserId + " is a wrong user id");
            }
            return notificationRepository.findAllByReceiverUserId(receiverUserId)
                                         .stream()
                                         .map(entity -> {
                                             Optional<User> sender = userRepository.findById(entity.getSenderUserId());
                                             Optional<User> receiver = userRepository.findById(entity.getReceiverUserId());
                                             Optional<Post> post = postRepository.findById(entity.getPostId());
                                             return Notification.builder()
                                                                .id(entity.getId())
                                                                .senderUserId(sender.get().getId())
                                                                .senderUserName(sender.get().getUsername())
                                                                .receiverUserId(receiver.get().getId())
                                                                .receiverUserName(receiver.get().getUsername())
                                                                .postId(post.get().getId())
                                                                .postTitle(post.get().getName())
                                                                .eventCategory(EventCategory.of(entity.getEventCategory()))
                                                                .timestamp(entity.getTimestamp())
                                                                .read(entity.isStale())
                                                                .build();
                                         })
                                         .collect(Collectors.toList());
        });
    }

    public CompletableFuture<Void> notify(long senderUserid, long receiverUserId, long postId, EventCategory eventCategory) {
        final long timestamp = System.currentTimeMillis() / 1000;
        return CompletableFuture.supplyAsync(() -> {
            if (senderUserid <= 0) {
                throw new IllegalArgumentException(senderUserid + " is a wrong user id");
            }
            if (receiverUserId <= 0) {
                throw new IllegalArgumentException(receiverUserId + " is a wrong user id");
            }
            if (postId <= 0) {
                throw new IllegalArgumentException(postId + " is a wrong post id");
            }
            notificationRepository.save(NotificationEntity.builder()
                                                          .senderUserId(senderUserid)
                                                          .receiverUserId(receiverUserId)
                                                          .postId(postId)
                                                          .eventCategory(eventCategory.getValue())
                                                          .timestamp(timestamp)
                                                          .stale(false)
                                                          .build());
            return null;
        });
    }

    public CompletableFuture<Void> removeAll(long receiverUserId) {
        return CompletableFuture.supplyAsync(() -> {
            if (receiverUserId <= 0) {
                throw new IllegalArgumentException(receiverUserId + "is a wrong user id");
            }
            notificationRepository.findAllByReceiverUserId(receiverUserId)
                                  .forEach(notificationRepository::delete);
            return null;
        });
    }

    public CompletableFuture<Void> markAsReadAll(long receiverUserId) {
        return CompletableFuture.supplyAsync(() -> {
            if (receiverUserId <= 0) {
                throw new IllegalArgumentException(receiverUserId + "is a wrong user id");
            }
            notificationRepository.findAllByReceiverUserId(receiverUserId)
                                  .forEach(notification -> {
                                      notification.setStale(true);
                                      notificationRepository.save(notification);
                                  });
            return null;
        });
    }

    public CompletableFuture<Void> markAsRead(long receiverUserId, long notificationId) {
        return CompletableFuture.supplyAsync(() -> {
            if (notificationId <= 0) {
                throw new IllegalArgumentException(notificationId + " is a wrong notification id");
            }
            Optional<NotificationEntity> entity = notificationRepository.findById(notificationId);
            if (entity.isEmpty()) {
                throw new IllegalArgumentException(notificationId + " does not exist");
            }
            if (Objects.equals(receiverUserId, entity.get().getReceiverUserId()) == false) {
                throw new IllegalArgumentException(receiverUserId + " is not a owner of notification of " + notificationId);
            }
            entity.get().setStale(true);
            notificationRepository.save(entity.get());
            return null;
        });
    }
}
