package com.wellseecoding.server.infra.sns;

import java.util.Map;

public interface SnsUserInfoMapper {
    SnsUserInfo map(Map<String, Object> attributes);
}
