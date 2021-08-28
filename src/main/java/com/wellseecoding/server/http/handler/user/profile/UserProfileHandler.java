package com.wellseecoding.server.http.handler.user.profile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserProfileHandler {
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return ServerResponse.ok().build();
    }
}
