package com.wellseecoding.server.infra.jwt;

public interface JwtTokenMapper {
    String serialize(String payload);
    String deserialize(String token);
}
