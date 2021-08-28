package com.wellseecoding.server.http.token;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;

@Configuration
public class TokenConfig {
    @Value("${wellseecoding.http.access-token.shared-secret}")
    private String sharedSecret;
    @Value("${wellseecoding.http.access-token.claims.issuer}")
    private String issuer;
    @Value("${wellseecoding.http.access-token.timeout}")
    private long timeout;

    @Bean
    public AccessTokenGenerator accessTokenGenerator() {
        try {
            JWSSigner jwsSigner = new MACSigner(Hex.decodeHex(sharedSecret));
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            return new AccessTokenGenerator(
                    jwsSigner,
                    jwsHeader,
                    issuer,
                    Duration.ofSeconds(timeout)
            );
        } catch (Exception e) {
            throw new TokenConfigException("creating access token generator failed", e);
        }
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(Hex.decodeHex(sharedSecret), "HmacSHA256");
            return NimbusReactiveJwtDecoder.withSecretKey(secretKeySpec)
                                           .macAlgorithm(MacAlgorithm.HS256)
                                           .build();
        } catch (Exception e) {
            throw new TokenConfigException("creating jwt decoder failed", e);
        }
    }
}
