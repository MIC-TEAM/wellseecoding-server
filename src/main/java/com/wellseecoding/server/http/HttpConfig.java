package com.wellseecoding.server.http;

import com.wellseecoding.server.http.token.AccessTokenMapper;
import com.wellseecoding.server.infra.jwt.JwtTokenMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @Value("${wellseecoding.http.access-token.duration-in-seconds}")
    private long accessTokenDuration;

    @Bean
    public AccessTokenMapper accessTokenMapper(JwtTokenMapper jwtTokenMapper) {
        return new AccessTokenMapper(jwtTokenMapper, accessTokenDuration);
    }
}
