package com.wellseecoding.server.http.handler.user.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    @Builder.Default
    private final String status = StringUtils.EMPTY;
    @Builder.Default
    private final String aboutMe = StringUtils.EMPTY;
    @Builder.Default
    private final String job = StringUtils.EMPTY;
    @Builder.Default
    private final List<String> tags = Collections.emptyList();
    @Builder.Default
    private final List<Education> educations = Collections.emptyList();
    @Builder.Default
    private final List<Work> works = Collections.emptyList();
    @Builder.Default
    private final List<Link> links = Collections.emptyList();
}
