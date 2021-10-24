package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventCategory {
    MEMBER_APPLIED(0),
    MEMBER_APPROVED(1),
    COMMENT_ADDED(2);

    private long value;

    public static EventCategory of(long value) {
        if (MEMBER_APPLIED.getValue() == value) {
            return MEMBER_APPLIED;
        } else if (MEMBER_APPROVED.getValue() == value) {
            return MEMBER_APPROVED;
        } else if (COMMENT_ADDED.getValue() == value) {
            return COMMENT_ADDED;
        } else {
            throw new IllegalArgumentException(value + " is a wrong event category");
        }
    }
}
