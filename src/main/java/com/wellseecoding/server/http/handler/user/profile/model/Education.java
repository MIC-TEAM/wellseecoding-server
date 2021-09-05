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
public class Education {
    @Builder.Default
    private final String degree = StringUtils.EMPTY;
    @Builder.Default
    private final String major = StringUtils.EMPTY;
    @Builder.Default
    private final boolean graduated = false;
}
