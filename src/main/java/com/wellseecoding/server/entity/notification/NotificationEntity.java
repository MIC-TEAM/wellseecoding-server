package com.wellseecoding.server.entity.notification;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "sender_user_id")
    private Long senderUserId;
    @Column(name = "receiver_user_id")
    private Long receiverUserId;
    @Column(name = "post_id")
    private Long postId;
    @Column(name = "event_category")
    private Long eventCategory;
    private Long timestamp;
    private boolean stale;
}
