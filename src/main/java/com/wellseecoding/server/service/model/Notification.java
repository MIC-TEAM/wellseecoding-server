package com.wellseecoding.server.service.model;

import com.wellseecoding.server.entity.notification.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private Long postId;
    private EventCategory eventCategory;
    private Long timestamp;
    private boolean read;

    public static Notification valueOf(NotificationEntity entity) {
        return Notification.builder()
                           .id(entity.getId())
                           .userId(entity.getUserId())
                           .postId(entity.getPostId())
                           .eventCategory(EventCategory.of(entity.getEventCategory()))
                           .timestamp(entity.getTimestamp())
                           .read(entity.isStale())
                           .build();
    }
}
