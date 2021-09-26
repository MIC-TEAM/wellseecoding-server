package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ThemedPost {
    private String theme;
    private List<Post> posts;
}
