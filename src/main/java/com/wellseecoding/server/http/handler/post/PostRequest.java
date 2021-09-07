package com.wellseecoding.server.http.handler.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    @Builder.Default
    private final String name = StringUtils.EMPTY;
    @Builder.Default
    private final String deadline = StringUtils.EMPTY;
    @Builder.Default
    private final String schedule = StringUtils.EMPTY;
    @Builder.Default
    private final String summary = StringUtils.EMPTY;
    @Builder.Default
    private final String qualification = StringUtils.EMPTY;
    @Builder.Default
    private final String size = StringUtils.EMPTY;
    @Builder.Default
    private final String tags = StringUtils.EMPTY;
}
