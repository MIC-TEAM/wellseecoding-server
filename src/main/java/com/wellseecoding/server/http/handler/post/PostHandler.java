package com.wellseecoding.server.http.handler.post;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.http.handler.user.register.GroupResponse;
import com.wellseecoding.server.service.GroupService;
import com.wellseecoding.server.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@AllArgsConstructor
@Component
public class PostHandler {
    private final PostService postService;
    private final GroupService groupService;

    public Mono<ServerResponse> write(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(PostRequest.class))
                   .flatMap(tuple -> Mono.fromFuture(postService.write(tuple.getT1(), tuple.getT2())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return Mono.just(serverRequest.queryParam("keyword"))
                   .flatMap(input -> {
                       if (input.isEmpty()) {
                           return Mono.fromFuture(postService.getRandomPosts());
                       } else {
                           List<String> keywords = Arrays.asList(StringUtils.split(input.get(), " "));
                           return Mono.fromFuture(postService.searchPosts(keywords));
                       }
                   })
                   .flatMap(posts -> ServerResponse.ok().body(BodyInserters.fromValue(posts)));
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        return getPostId(request)
                .flatMap(postId -> Mono.fromFuture(postService.find(postId)))
                .flatMap(post -> ServerResponse.ok().body(BodyInserters.fromValue(post)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> remove(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(tuple -> {
                    final Long postId = tuple.getT1();
                    final Long userId = tuple.getT2();
                    return Mono.fromFuture(postService.delete(postId, userId));
                })
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> overwrite(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .zipWith(request.bodyToMono(PostRequest.class))
                .flatMap(triple -> {
                    final PostRequest postRequest = triple.getT2();
                    final Long postId = triple.getT1().getT1();
                    final Long userId = triple.getT1().getT2();
                    return Mono.fromFuture(postService.overwrite(postId, userId, postRequest));
                })
                .then(ServerResponse.ok().build());
    }

    private Mono<Long> getPostId(ServerRequest request) {
        Map<String, String> pathVariables = request.pathVariables();
        String postId = pathVariables.get("id");
        if (nonNull(postId)) {
            return Mono.just(Long.valueOf(postId));
        } else {
            return Mono.empty();
        }
    }

    public Mono<ServerResponse> getGroups(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(tuple -> {
                    final long postId = tuple.getT1();
                    final long userId = tuple.getT2();
                    return Mono.fromFuture(groupService.getMembersForPost(userId, postId));
                })
                .flatMap(members -> ServerResponse.ok().body(BodyInserters.fromValue(new GroupResponse(members))));
    }

    public Mono<ServerResponse> applyAsMember(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(tuple -> {
                    final long postId = tuple.getT1();
                    final long userId = tuple.getT2();
                    return Mono.fromFuture(groupService.applyAsMember(userId, postId));
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(IllegalArgumentException.class, e -> {
                    return ServerResponse.badRequest()
                                         .body(BodyInserters.fromValue(new ErrorResponse(e.getMessage())));
                });
    }

    private Mono<Long> getTargetUserId(ServerRequest request) {
        Map<String, String> pathVariables = request.pathVariables();
        String userId = pathVariables.get("userId");
        if (nonNull(userId)) {
            return Mono.just(Long.valueOf(userId));
        } else {
            return Mono.empty();
        }
    }

    public Mono<ServerResponse> acceptApplicant(ServerRequest request) {
        return getPostId(request)
                .zipWith(getTargetUserId(request))
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(triple -> {
                    final long postId = triple.getT1().getT1();
                    final long targetUserId = triple.getT1().getT2();
                    final long sourceUserId = triple.getT2();
                    return Mono.fromFuture(groupService.acceptApplicant(sourceUserId, targetUserId, postId));
                })
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getLinkPerPost(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .flatMap(tuple -> {
                    final long postId = tuple.getT1();
                    final long userId = tuple.getT2();
                    return Mono.fromFuture(postService.getLinkPerPost(postId, userId));
                })
                .flatMap(link -> ServerResponse.ok().body(BodyInserters.fromValue(link)));
    }

    public Mono<ServerResponse> putLinkPerPost(ServerRequest request) {
        return getPostId(request)
                .zipWith(Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID))))
                .zipWith(getNewLink(request))
                .flatMap(triple -> {
                    final long postId = triple.getT1().getT1();
                    final long userId = triple.getT1().getT2();
                    final String newLink = triple.getT2();
                    return Mono.fromFuture(postService.setLinkPerPost(postId, userId, newLink));
                })
                .then(ServerResponse.ok().build());
    }

    private Mono<String> getNewLink(ServerRequest request) {
        return request.bodyToMono(LinkPerPostRequest.class)
                      .map(LinkPerPostRequest::getLink);
    }
}
