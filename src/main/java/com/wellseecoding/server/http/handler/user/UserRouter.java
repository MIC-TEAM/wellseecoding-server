package com.wellseecoding.server.http.handler.user;

import com.wellseecoding.server.http.filter.UserIdExtractor;
import com.wellseecoding.server.http.handler.user.profile.UserProfileHandler;
import com.wellseecoding.server.http.handler.user.register.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class UserRouter {
    @Bean
    public RouterFunction<ServerResponse> routeUserRequests(UserHandler userHandler, UserProfileHandler userProfileHandler) {
        return RouterFunctions.route().path("/api/v1/users", builder -> {
            builder.path("/profile", profileBuilder -> {
                profileBuilder.PUT("/status", contentType(MediaType.APPLICATION_JSON), userProfileHandler::setStatus)
                              .PUT("/preface", contentType(MediaType.APPLICATION_JSON), userProfileHandler::setPreface)
                              .PUT("/education", contentType(MediaType.APPLICATION_JSON), userProfileHandler::setEducation)
                              .PUT("/works", contentType(MediaType.APPLICATION_JSON), userProfileHandler::setWorks)
                              .PUT("/links", contentType(MediaType.APPLICATION_JSON), userProfileHandler::setLinks)
                              .GET("", userProfileHandler::get)
                              .filter(new UserIdExtractor());
            }).path("/likes", likeBuilder -> {
                likeBuilder.GET("", userHandler::getLikes)
                           .POST("", contentType(MediaType.APPLICATION_JSON), userHandler::addLike)
                           .DELETE("", contentType(MediaType.APPLICATION_JSON), userHandler::removeLike)
                           .filter(new UserIdExtractor());
            }).path("/token", loginBuilder -> {
                loginBuilder.POST("", contentType(MediaType.APPLICATION_JSON), userHandler::handleLogin);
            }).POST(contentType(MediaType.APPLICATION_JSON), userHandler::handleRegister);
        }).build();
    }
}
