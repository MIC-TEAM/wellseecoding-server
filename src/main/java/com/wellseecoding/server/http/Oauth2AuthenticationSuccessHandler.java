package com.wellseecoding.server.http;

import com.wellseecoding.server.infra.sns.SnsUserInfo;
import com.wellseecoding.server.infra.sns.SnsUserInfoResolver;
import com.wellseecoding.server.service.UserService;
import com.wellseecoding.server.user.sns.SnsInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public class Oauth2AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    private final ServerAuthenticationSuccessHandler delegate;
    private final UserService userService;
    private final boolean shouldEnforceStrictSameSite;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return getOrSaveSnsUser(authentication)
                .doOnNext(snsInfo -> {
                    final String refreshToken = snsInfo.getUser().getRefreshToken();
                    final ResponseCookie refreshTokenCookie = generateCookieForRefreshToken(refreshToken);
                    webFilterExchange.getExchange().getResponse().addCookie(refreshTokenCookie);
                })
                .then(delegate.onAuthenticationSuccess(webFilterExchange, authentication));
    }

    private Mono<SnsInfo> getOrSaveSnsUser(Authentication authentication) {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            final String snsType = token.getAuthorizedClientRegistrationId();
            final Map<String, Object> snsAttributes = token.getPrincipal().getAttributes();
            SnsUserInfo snsUserInfo = SnsUserInfoResolver.resolve(snsType, snsAttributes);
            return Mono.fromFuture(userService.mapSnsUser(snsUserInfo.getUserId(),
                                                          snsUserInfo.getSnsType(),
                                                          snsUserInfo.getUserName(),
                                                          snsUserInfo.getEmail()));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private ResponseCookie generateCookieForRefreshToken(String refreshToken) {
        if (shouldEnforceStrictSameSite) {
            return ResponseCookie.from(CookieNameRegistry.REFRESH_TOKEN, refreshToken)
                                 .httpOnly(true)
                                 .sameSite("Strict")
                                 .build();
        } else {
            return ResponseCookie.from(CookieNameRegistry.REFRESH_TOKEN, refreshToken)
                                 .httpOnly(true)
                                 .sameSite("None")
                                 .secure(true)
                                 .build();
        }
    }
}
