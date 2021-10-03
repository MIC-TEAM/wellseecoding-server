package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Comment {
    private final long userId;
    private final String userName;
    private final long commentId;
    private final long commentDate;
    private final String text;
    private final boolean deleted;
    private final List<Comment> children;
}
