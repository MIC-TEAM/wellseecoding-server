package com.wellseecoding.server.http.handler.comment;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CommentHandler {
    private final CommentService commentService;

    public Mono<ServerResponse> addComment(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> {
            long userId = contextView.get(ContextNameRegistry.USER_ID);
            return Mono.just(userId);
        }).zipWith(serverRequest.bodyToMono(CommentRequest.class)).flatMap(tuple -> {
            final long userId = tuple.getT1();
            final CommentRequest commentRequest = tuple.getT2();
            return Mono.fromFuture(commentService.addComment(userId,
                                                             Long.parseLong(serverRequest.pathVariable("postId")),
                                                             commentRequest.getParentId(),
                                                             commentRequest.getText()));
        }).then(ServerResponse.ok().build());
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
