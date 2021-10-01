package com.wellseecoding.server.http.handler.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    @Builder.Default
    private final List<String> keywords = new ArrayList<>();
}
