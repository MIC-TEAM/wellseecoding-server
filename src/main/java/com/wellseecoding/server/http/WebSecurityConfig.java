package com.wellseecoding.server.http;

import com.wellseecoding.server.http.token.AccessTokenMapper;
import com.wellseecoding.server.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Value("${wellseecoding.http.uris.oauth2-success}")
    private String authSuccessPage;
    @Value("${wellseecoding.http.uris.oauth2-failure}")
    private String authFailurePage;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AccessTokenMapper accessTokenMapper, UserService userService) {
        final RedirectServerAuthenticationSuccessHandler authSuccessHandler = new RedirectServerAuthenticationSuccessHandler(authSuccessPage);
        return http
                .oauth2Login()
                .authenticationSuccessHandler(new Oauth2AuthenticationSuccessHandler(authSuccessHandler, accessTokenMapper, userService))
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler(authFailurePage))
                .and()
                .build();
    }
}
