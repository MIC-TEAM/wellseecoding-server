package com.wellseecoding.server.http.handler.comment;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
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
        return Mono.defer(() -> {
            final long postId = Long.parseLong(serverRequest.pathVariable("postId"));
            return Mono.fromFuture(commentService.getComments(postId));
        }).flatMap(comments -> ServerResponse.ok().body(BodyInserters.fromValue(comments)));
    }

    public Mono<ServerResponse> updateComment(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> {
            long userId = contextView.get(ContextNameRegistry.USER_ID);
            return Mono.just(userId);
        }).zipWith(serverRequest.bodyToMono(CommentRequest.class)).flatMap(tuple -> {
            final long userId = tuple.getT1();
            final CommentRequest commentRequest = tuple.getT2();
            final long commentId = Long.parseLong(serverRequest.pathVariable("commentId"));
            return Mono.fromFuture(commentService.updateComment(userId,
                                                                commentId,
                                                                commentRequest.getText()));
        }).then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteComment(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> {
            long userId = contextView.get(ContextNameRegistry.USER_ID);
            return Mono.just(userId);
        }).flatMap(userId -> {
            final long commentId = Long.parseLong(serverRequest.pathVariable("commentId"));
            return Mono.fromFuture(commentService.deleteComment(userId, commentId));
        }).then(ServerResponse.ok().build());
    }
}
