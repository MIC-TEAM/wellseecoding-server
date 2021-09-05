package com.wellseecoding.server.http.filter;

import com.wellseecoding.server.http.ContextNameRegistry;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

public class UserIdExtractor implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return request.principal().map(this::getUserId).flatMap(userId -> {
            return next.handle(request).contextWrite(context -> context.put(ContextNameRegistry.USER_ID, userId));
        });
    }

    private Long getUserId(Principal principal) {
        try {
            final JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            return Long.valueOf(token.getName());
        } catch (Exception e) {
            throw new UserIdNotFoundException(e);
        }
    }
}
