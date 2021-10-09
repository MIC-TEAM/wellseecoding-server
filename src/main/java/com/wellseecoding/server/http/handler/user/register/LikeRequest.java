package com.wellseecoding.server.http.handler.user.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequest {
    @Builder.Default
    private final long postId = 0;
}
