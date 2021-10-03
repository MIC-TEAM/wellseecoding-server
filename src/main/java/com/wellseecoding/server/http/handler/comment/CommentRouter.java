package com.wellseecoding.server.http.handler.comment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class CommentRouter {
    @Bean
    public RouterFunction<ServerResponse> routeCommentRequests(CommentHandler commentHandler) {
        return RouterFunctions.route().path("/api/v1/posts/{postId}/comments", rootBuilder -> {
            rootBuilder.GET("", commentHandler::getComments)
                       .POST("", contentType(MediaType.APPLICATION_JSON), commentHandler::addComment)
                       .path("/{commentId}", builder -> {
                           builder.PUT("", contentType(MediaType.APPLICATION_JSON), commentHandler::updateComment)
                                  .DELETE("", contentType(MediaType.APPLICATION_JSON), commentHandler::deleteComment);
                       });
        }).build();
    }
}
