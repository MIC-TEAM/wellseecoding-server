package com.wellseecoding.server.http.sns;

import com.wellseecoding.server.http.CookieNameRegistry;
import com.wellseecoding.server.http.token.AccessTokenGenerator;
import com.wellseecoding.server.infra.sns.SnsUserInfo;
import com.wellseecoding.server.infra.sns.SnsUserInfoResolver;
import com.wellseecoding.server.service.UserService;
import com.wellseecoding.server.entity.sns.SnsInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public class SnsAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    private final ServerAuthenticationSuccessHandler delegate;
    private final AccessTokenGenerator accessTokenGenerator;
    private final UserService userService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return getOrSaveSnsUser(authentication)
                .doOnNext(snsInfo -> {
                    final String accessToken = accessTokenGenerator.generate(snsInfo.getUser().getId());
                    final ResponseCookie accessTokenCookie = ResponseCookie.from(CookieNameRegistry.ACCESS_TOKEN, accessToken).build();
                    webFilterExchange.getExchange().getResponse().addCookie(accessTokenCookie);
                })
                .doOnNext(snsInfo -> {
                    final String refreshToken = snsInfo.getUser().getRefreshToken();
                    final ResponseCookie refreshTokenCookie = ResponseCookie.from(CookieNameRegistry.REFRESH_TOKEN, refreshToken)
                                                                            .httpOnly(true)
                                                                            .build();
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
}
