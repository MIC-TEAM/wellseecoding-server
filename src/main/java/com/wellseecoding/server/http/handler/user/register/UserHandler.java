package com.wellseecoding.server.http.handler.user.register;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.http.CookieNameRegistry;
import com.wellseecoding.server.http.token.AccessTokenGenerator;
import com.wellseecoding.server.service.GroupService;
import com.wellseecoding.server.service.NotificationService;
import com.wellseecoding.server.service.UserService;
import com.wellseecoding.server.entity.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserHandler {
    private final AccessTokenGenerator accessTokenGenerator;
    private final UserService userService;
    private final GroupService groupService;
    private final NotificationService notificationService;

    public Mono<ServerResponse> handleRegister(ServerRequest request) {
        return request.bodyToMono(UserRegisterRequest.class)
                      .flatMap(userRegisterRequest -> {
                          return Mono.fromFuture(userService.createUser(userRegisterRequest.getUsername(),
                                                                        userRegisterRequest.getPassword(),
                                                                        userRegisterRequest.getEmail()));
                      })
                      .flatMap(this::createResponse);
    }

    public Mono<ServerResponse> handleLogin(ServerRequest request) {
        return request.bodyToMono(UserLoginRequest.class)
                      .flatMap(userRegisterRequest -> {
                          return Mono.fromFuture(userService.login(userRegisterRequest.getEmail(),
                                                                   userRegisterRequest.getPassword()));
                      })
                      .flatMap(this::createResponse);
    }

    private Mono<ServerResponse> createResponse(User user) {
        final String accessToken = accessTokenGenerator.generate(user.getId(), user.getUsername());
        final ResponseCookie accessTokenCookie = ResponseCookie.from(CookieNameRegistry.ACCESS_TOKEN, accessToken).build();

        final String refreshToken = user.getRefreshToken();
        final ResponseCookie refreshTokenCookie = ResponseCookie.from(CookieNameRegistry.REFRESH_TOKEN, refreshToken)
                                                                .httpOnly(true)
                                                                .build();

        return ServerResponse.ok()
                             .cookie(accessTokenCookie)
                             .cookie(refreshTokenCookie)
                             .build();
    }

    public Mono<ServerResponse> getLikes(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> {
                       return Mono.fromFuture(userService.getLikes(userId).thenApply(Likes::new));
                   }).flatMap(likes -> ServerResponse.ok().body(BodyInserters.fromValue(likes)));
    }

    public Mono<ServerResponse> addLike(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(request.bodyToMono(LikeRequest.class))
                   .flatMap(tuple -> {
                       final long userId = tuple.getT1();
                       final long postId = tuple.getT2().getPostId();
                       return Mono.fromFuture(userService.addLike(userId, postId));
                   })
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> removeLike(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(request.bodyToMono(LikeRequest.class))
                   .flatMap(tuple -> {
                       final long userId = tuple.getT1();
                       final long postId = tuple.getT2().getPostId();
                       return Mono.fromFuture(userService.removeLike(userId, postId));
                   })
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getGroups(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(groupService.getGroupsForUser(userId)))
                   .flatMap(groups -> ServerResponse.ok().body(BodyInserters.fromValue(new GroupResponse(groups))));
    }

    public Mono<ServerResponse> getRegisteredGroups(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(groupService.getRegisteredGroups(userId)))
                   .flatMap(groups -> ServerResponse.ok().body(BodyInserters.fromValue(new RegisteredGroupResponse(groups))));
    }

    public Mono<ServerResponse> getNotifications(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(notificationService.getAllNotifications(userId)))
                   .flatMap(notifications -> ServerResponse.ok().body(BodyInserters.fromValue(new NotificationResponse(notifications))));
    }

    public Mono<ServerResponse> removeAllNotifications(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(notificationService.removeAll(userId)))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> markAsReadAll(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(notificationService.markAsReadAll(userId)))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> markAsRead(ServerRequest request) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> {
                       final long notificationId = Long.parseLong(request.pathVariable("notificationId"));
                       return Mono.fromFuture(notificationService.markAsRead(userId, notificationId));
                   })
                   .then(ServerResponse.ok().build());
    }
}
