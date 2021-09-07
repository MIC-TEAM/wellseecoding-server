package com.wellseecoding.server.http.handler.post;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class PostHandler {
    private final PostService postService;

    public Mono<ServerResponse> write(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(PostRequest.class))
                   .flatMap(tuple -> Mono.fromFuture(postService.write(tuple.getT1(), tuple.getT2())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return Mono.fromFuture(postService.findAll())
                   .flatMap(posts -> ServerResponse.ok().body(BodyInserters.fromValue(posts)));
    }
}
