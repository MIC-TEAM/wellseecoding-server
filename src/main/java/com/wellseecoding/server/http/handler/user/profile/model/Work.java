package com.wellseecoding.server.http.handler.user.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Work {
    @Builder.Default
    private final String role = StringUtils.EMPTY;
    @Builder.Default
    private final String technology = StringUtils.EMPTY;
    @Builder.Default
    private final long years = 0;
}

