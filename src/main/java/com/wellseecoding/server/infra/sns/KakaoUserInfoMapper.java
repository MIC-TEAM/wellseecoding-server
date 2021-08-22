package com.wellseecoding.server.infra.sns;

import java.util.Map;

public class KakaoUserInfoMapper implements SnsUserInfoMapper {
    public static final String MAPPER_KEY = "KAKAO";

    @Override
    public SnsUserInfo map(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        final String userId = String.valueOf(attributes.get("id"));
        final String userName = (String) profile.get("nickname");
        final String email = (String) kakaoAccount.get("email");
        return new SnsUserInfo("kakao", userId, userName, email);
    }
}
