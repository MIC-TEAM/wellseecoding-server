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
public class Link {
    @Builder.Default
    private final String name = StringUtils.EMPTY;
    @Builder.Default
    private final String link = StringUtils.EMPTY;
    @Builder.Default
    private final String description = StringUtils.EMPTY;
}
