package com.wellseecoding.server.http.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellseecoding.server.infra.jwt.JwtTokenMapper;
import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class AccessTokenMapper {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenMapper jwtTokenMapper;
    private final long accessTokenDuration;

    public String serialize(long userId) {
        final long expireAt = Instant.now().getEpochSecond() + accessTokenDuration;
        return serialize(userId, expireAt);
    }

    public String serialize(long userId, long expireAt) {
        try {
            final PlainTextAccessToken plainTextAccessToken = new PlainTextAccessToken(userId, expireAt);
            final String plainTextAccessTokenString = objectMapper.writeValueAsString(plainTextAccessToken);
            return jwtTokenMapper.serialize(plainTextAccessTokenString);
        } catch (Exception e) {
            throw new AccessTokenSerializeException(e);
        }
    }

    public PlainTextAccessToken deserialize(String accessTokenString) {
        try {
            final String plainTextAccessToken = jwtTokenMapper.deserialize(accessTokenString);
            return objectMapper.readValue(plainTextAccessToken, new TypeReference<>() {});
        } catch (Exception e) {
            throw new AccessTokenDeserializeException(e);
        }
    }
}
