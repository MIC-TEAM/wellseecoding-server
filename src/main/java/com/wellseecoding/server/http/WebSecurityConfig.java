package com.wellseecoding.server.http;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Value("${wellseecoding.http.cors.origins}")
    private String corsOrigins;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity,
                                                         ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler,
                                                         ServerAuthenticationFailureHandler serverAuthenticationFailureHandler) {
        return serverHttpSecurity
                .httpBasic(HttpBasicSpec::disable)
                .csrf(CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers("/api/v1/users/profile/**").authenticated()
                                         .pathMatchers("/api/v1/posts/**").authenticated()
                                         .anyExchange().permitAll();
                })
                .oauth2Login(oAuth2LoginSpec -> {
                    oAuth2LoginSpec.authenticationSuccessHandler(serverAuthenticationSuccessHandler)
                                   .authenticationFailureHandler(serverAuthenticationFailureHandler);
                })
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(StringUtils.split(corsOrigins, ",")));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
