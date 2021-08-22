package com.wellseecoding.server.infra;

import com.wellseecoding.server.infra.jwt.DirectJweTokenMapper;
import com.wellseecoding.server.infra.jwt.JwtTokenMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Value("${wellseecoding.jwt.jwe.encryption-algorithm}")
    private String jweEncryptionAlgorithm;
    @Value("${wellseecoding.jwt.jwe.key}")
    private String jweKey;

    @Bean
    public JwtTokenMapper jwtTokenMapper() {
        return new DirectJweTokenMapper(jweEncryptionAlgorithm, jweKey);
    }
}
