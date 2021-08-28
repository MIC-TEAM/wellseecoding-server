package com.wellseecoding.server.http.sns;

import com.wellseecoding.server.http.token.AccessTokenGenerator;
import com.wellseecoding.server.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

@Configuration
public class SnsConfig {
    @Value("${wellseecoding.http.uris.sns-success}")
    private String snsSuccessPage;
    @Value("${wellseecoding.http.uris.sns-failure}")
    private String snsFailurePage;

    @Bean
    public ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler(UserService userService, AccessTokenGenerator accessTokenGenerator) {
        return new SnsAuthenticationSuccessHandler(
                new RedirectServerAuthenticationSuccessHandler(snsSuccessPage),
                accessTokenGenerator,
                userService
        );
    }

    @Bean
    public ServerAuthenticationFailureHandler serverAuthenticationFailureHandler() {
        return new RedirectServerAuthenticationFailureHandler(snsFailurePage);
    }
}
