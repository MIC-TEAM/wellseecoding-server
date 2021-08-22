package com.wellseecoding.server.infra.sns;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class SnsUserInfoResolver {
    private static final Map<String, SnsUserInfoMapper> mappers = new HashMap<>() {{
        put(NaverUserInfoMapper.MAPPER_KEY.toLowerCase(), new NaverUserInfoMapper());
        put(KakaoUserInfoMapper.MAPPER_KEY.toLowerCase(), new KakaoUserInfoMapper());
    }};

    public static SnsUserInfo resolve(@NonNull String snsType, @NonNull Map<String, Object> attributes) {
        final SnsUserInfoMapper mapper = mappers.get(snsType.toLowerCase());
        if (isNull(mapper)) {
            throw new IllegalArgumentException("user info mapper for " + snsType + " is not found");
        }

        return mapper.map(attributes);
    }
}
