package com.wellseecoding.server.http;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity,
                                                         ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler,
                                                         ServerAuthenticationFailureHandler serverAuthenticationFailureHandler) {
        return serverHttpSecurity
                .httpBasic(HttpBasicSpec::disable)
                .csrf(CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers("/api/v1/users/profile/**").authenticated()
                                         .anyExchange().permitAll();
                })
                .oauth2Login(oAuth2LoginSpec -> {
                    oAuth2LoginSpec.authenticationSuccessHandler(serverAuthenticationSuccessHandler)
                                   .authenticationFailureHandler(serverAuthenticationFailureHandler);
                })
                .build();
    }
}
