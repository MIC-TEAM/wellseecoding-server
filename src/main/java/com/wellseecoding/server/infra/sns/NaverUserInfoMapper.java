package com.wellseecoding.server.infra.sns;

import java.util.Map;

public class NaverUserInfoMapper implements SnsUserInfoMapper {
    public static final String MAPPER_KEY = "NAVER";

    @Override
    public SnsUserInfo map(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        final String userId = (String) response.get("id");
        final String userName = (String) response.get("name");
        final String email = (String) response.get("email");
        return new SnsUserInfo("naver", userId, userName, email);
    }
}
