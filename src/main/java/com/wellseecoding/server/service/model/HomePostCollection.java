package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HomePostCollection {
    @Builder.Default
    private final List<Post> createdGroups = Collections.emptyList();
    @Builder.Default
    private final List<Post> registeredGroups = Collections.emptyList();
    @Builder.Default
    private final List<Post> appliedGroups = Collections.emptyList();
    @Builder.Default
    private final List<Post> likedGroups = Collections.emptyList();
}
