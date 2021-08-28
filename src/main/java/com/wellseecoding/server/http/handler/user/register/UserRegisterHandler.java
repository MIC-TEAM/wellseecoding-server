package com.wellseecoding.server.http.handler.user.register;

import com.wellseecoding.server.http.CookieNameRegistry;
import com.wellseecoding.server.http.handler.OperationResult;
import com.wellseecoding.server.http.token.AccessTokenGenerator;
import com.wellseecoding.server.service.UserService;
import com.wellseecoding.server.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserRegisterHandler {
    private static final OperationResult SUCCESS_RESULT = OperationResult.builder()
                                                                         .success(true)
                                                                         .description("user has been added")
                                                                         .build();

    private final AccessTokenGenerator accessTokenGenerator;
    private final UserService userService;

    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(UserRegisterRequest.class)
                      .flatMap(userRegisterRequest -> {
                          return Mono.fromFuture(userService.createUser(userRegisterRequest.getUsername(),
                                                                        userRegisterRequest.getPassword(),
                                                                        userRegisterRequest.getEmail()));
                      })
                      .flatMap(this::createResponse);
    }

    private Mono<ServerResponse> createResponse(User user) {
        final String accessToken = accessTokenGenerator.generate(user.getId());
        final ResponseCookie accessTokenCookie = ResponseCookie.from(CookieNameRegistry.ACCESS_TOKEN, accessToken).build();

        final String refreshToken = user.getRefreshToken();
        final ResponseCookie refreshTokenCookie = ResponseCookie.from(CookieNameRegistry.REFRESH_TOKEN, refreshToken)
                                                                .httpOnly(true)
                                                                .build();

        return ServerResponse.ok()
                             .cookie(accessTokenCookie)
                             .cookie(refreshTokenCookie)
                             .body(BodyInserters.fromValue(SUCCESS_RESULT));
    }
}
