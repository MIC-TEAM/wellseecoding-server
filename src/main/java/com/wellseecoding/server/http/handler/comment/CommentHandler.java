package com.wellseecoding.server.http.handler.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CommentHandler {
    public Mono<ServerResponse> addComment(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> getComments(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> updateComment(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> deleteComment(ServerRequest serverRequest) {
        return Mono.empty();
    }
}
