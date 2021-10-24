package com.wellseecoding.server.http.handler.user.register;

import com.wellseecoding.server.service.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private final List<Notification> notifications;
}
