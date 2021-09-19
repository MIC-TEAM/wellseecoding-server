package com.wellseecoding.server.http.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
public class AccessTokenGenerator {
    private final JWSSigner jwsSigner;
    private final JWSHeader jwsHeader;
    private final String issuer;
    private final Duration timeout;

    public String generate(long userId, String name) {
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet
                    .Builder()
                    .issuer(issuer)
                    .expirationTime(Date.from(Instant.now().plus(timeout)))
                    .subject(String.valueOf(userId))
                    .claim(PrivateTokenNameRegistry.USER_NAME, name)
                    .build();
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AccessTokenGeneratorException(e);
        }
    }
}
