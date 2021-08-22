package com.wellseecoding.server.infra.sns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnsUserInfo {
    private final String snsType;
    private final String userId;
    private final String userName;
    private final String email;
}
