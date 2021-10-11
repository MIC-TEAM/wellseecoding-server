package com.wellseecoding.server.http.handler.post;

import com.wellseecoding.server.http.filter.UserIdExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class PostRouter {
    @Bean
    public RouterFunction<ServerResponse> routePostRequests(PostHandler postHandler) {
        return RouterFunctions.route().path("/api/v1/posts", postsBuilder -> {
            postsBuilder.POST("", contentType(MediaType.APPLICATION_JSON), postHandler::write)
                        .GET("", postHandler::getAll)
                        .path("/{id}", postBuilder -> {
                            postBuilder.GET("", postHandler::get)
                                       .DELETE("", postHandler::remove)
                                       .PUT("", contentType(MediaType.APPLICATION_JSON), postHandler::overwrite)
                                       .path("/members", groupBuilder -> {
                                           groupBuilder.GET("", postHandler::getGroups)
                                                       .POST("", postHandler::applyAsMember)
                                                       .PUT("/{userId}", postHandler::acceptApplicant);
                                       });
                        })
                        .filter(new UserIdExtractor());
        }).build();
    }
}
