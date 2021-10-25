package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long senderUserId;
    private String senderUserName;
    private Long receiverUserId;
    private String receiverUserName;
    private Long postId;
    private String postTitle;
    private EventCategory eventCategory;
    private Long timestamp;
    private boolean read;
}
