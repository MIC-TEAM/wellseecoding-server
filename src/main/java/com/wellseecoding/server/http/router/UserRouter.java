package com.wellseecoding.server.http.router;

import com.wellseecoding.server.http.handler.user.register.UserRegisterHandler;
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
    public RouterFunction<ServerResponse> routeUserRequests(UserRegisterHandler userRegisterHandler) {
        return RouterFunctions.route()
                              .POST("/api/v1/users", contentType(MediaType.APPLICATION_JSON), userRegisterHandler::handle)
                              .build();
    }
}
