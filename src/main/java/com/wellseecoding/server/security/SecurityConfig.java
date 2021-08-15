package com.wellseecoding.server.security;

import com.wellseecoding.server.security.jwt.DirectJweTokenMapper;
import com.wellseecoding.server.security.jwt.JwtTokenMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Value("${jwt.jwe.encryption-algorithm}")
    private String jweEncryptionAlgorithm;
    @Value("${jwt.jwe.key}")
    private String jweKey;

    @Bean
    public JwtTokenMapper jwtTokenMapper() {
        return new DirectJweTokenMapper(jweEncryptionAlgorithm, jweKey);
    }
}
