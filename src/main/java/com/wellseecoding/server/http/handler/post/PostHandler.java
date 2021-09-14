package com.wellseecoding.server.http.handler.post;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static java.util.Objects.nonNull;

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

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return Mono.fromFuture(postService.findAll())
                   .flatMap(posts -> ServerResponse.ok().body(BodyInserters.fromValue(posts)));
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        return getPostId(request)
                .flatMap(postId -> Mono.fromFuture(postService.find(postId)))
                .flatMap(post -> ServerResponse.ok().body(BodyInserters.fromValue(post)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> remove(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(tuple -> {
                    final Long postId = tuple.getT1();
                    final Long userId = tuple.getT2();
                    return Mono.fromFuture(postService.delete(postId, userId));
                })
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> overwrite(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .zipWith(request.bodyToMono(PostRequest.class))
                .flatMap(triple -> {
                    final PostRequest postRequest = triple.getT2();
                    final Long postId = triple.getT1().getT1();
                    final Long userId = triple.getT1().getT2();
                    return Mono.fromFuture(postService.overwrite(postId, userId, postRequest));
                })
                .then(ServerResponse.ok().build());
    }

    private Mono<Long> getPostId(ServerRequest request) {
        Map<String, String> pathVariables = request.pathVariables();
        String postId = pathVariables.get("id");
        if (nonNull(postId)) {
            return Mono.just(Long.valueOf(postId));
        } else {
            return Mono.empty();
        }
    }
}
