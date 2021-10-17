package com.wellseecoding.server.http.handler.user.home;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class HomeHandler {
    private final HomeService homeService;

    public Mono<ServerResponse> getHomePosts(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(homeService.getHomePosts(userId)))
                   .flatMap(homePostCollection -> ServerResponse.ok().body(BodyInserters.fromValue(homePostCollection)));
    }
}
