package com.wellseecoding.server.security.jwt;

public interface JwtTokenMapper {
    String serialize(String payload);
    String deserialize(String token);
}
