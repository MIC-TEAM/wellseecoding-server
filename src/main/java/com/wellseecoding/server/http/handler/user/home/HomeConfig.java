package com.wellseecoding.server.http.handler.user.home;

import com.wellseecoding.server.http.filter.UserIdExtractor;
import com.wellseecoding.server.service.HomeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HomeConfig {
    @Bean
    public HomeHandler homeHandler(HomeService homeService) {
        return new HomeHandler(homeService);
    }

    @Bean
    public RouterFunction<ServerResponse> homeRouter(HomeHandler homeHandler) {
        return RouterFunctions.route().path("/api/v1/home", builder -> {
            builder.GET("/posts", homeHandler::getHomePosts)
                   .filter(new UserIdExtractor());
        }).build();
    }
}
