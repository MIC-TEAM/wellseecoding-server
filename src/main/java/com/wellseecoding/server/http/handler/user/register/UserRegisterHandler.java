package com.wellseecoding.server.http.handler.user.register;

import com.wellseecoding.server.http.handler.OperationResult;
import com.wellseecoding.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserRegisterHandler {
    private static final Mono<ServerResponse> SUCCESS_RESPONSE = createSuccessResponse();

    private static Mono<ServerResponse> createSuccessResponse() {
        return ServerResponse.ok()
                             .body(BodyInserters.fromValue(OperationResult.builder()
                                                                          .success(true)
                                                                          .description("user has been added")
                                                                          .build()));
    }

    private final UserService userService;

    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(UserRegisterRequest.class)
                      .flatMap(userRegisterRequest -> {
                          return Mono.fromFuture(userService.createUser(userRegisterRequest.getUsername(),
                                                                        userRegisterRequest.getPassword(),
                                                                        userRegisterRequest.getEmail()));
                      })
                      .then(SUCCESS_RESPONSE);
    }
}
