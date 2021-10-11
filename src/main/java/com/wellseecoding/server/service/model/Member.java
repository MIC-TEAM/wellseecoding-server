package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {
    private final long userId;
    private final long postId;
    private final boolean authorized;
}
