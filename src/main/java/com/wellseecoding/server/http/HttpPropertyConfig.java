package com.wellseecoding.server.http;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class HttpPropertyConfig {
    @Value("${wellseecoding.http.cookie-domain}")
    private String cookieDomain;
    @Value("${wellseecoding.http.uris.sns-success}")
    private String snsSuccessPage;
    @Value("${wellseecoding.http.uris.sns-failure}")
    private String snsFailurePage;
    @Value("${wellseecoding.http.access-token.shared-secret}")
    private String sharedSecret;
    @Value("${wellseecoding.http.access-token.timeout}")
    private String timeout;
    @Value("${wellseecoding.http.access-token.claims.issuer}")
    private String issuer;
    @Value("${wellseecoding.http.cors.origins}")
    private String origins;
}
