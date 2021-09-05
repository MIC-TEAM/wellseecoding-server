package com.wellseecoding.server.http.handler.user.profile;

import com.wellseecoding.server.http.ContextNameRegistry;
import com.wellseecoding.server.http.handler.user.profile.model.Education;
import com.wellseecoding.server.http.handler.user.profile.model.Link;
import com.wellseecoding.server.http.handler.user.profile.model.Profile;
import com.wellseecoding.server.http.handler.user.profile.model.Work;
import com.wellseecoding.server.service.UserService;
import com.wellseecoding.server.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class UserProfileHandler {
    private final UserService userService;

    public Mono<ServerResponse> setStatus(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(Profile.class))
                   .flatMap(tuple -> Mono.fromFuture(userService.setStatus(tuple.getT1(), tuple.getT2().getStatus())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> setPreface(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(Profile.class))
                   .flatMap(tuple -> Mono.fromFuture(userService.setAboutMe(tuple.getT1(), tuple.getT2().getAboutMe())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> setEducation(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(Profile.class))
                   .flatMap(tuple -> Mono.fromFuture(userService.setEducations(tuple.getT1(), tuple.getT2().getEducations())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> setWorks(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(Profile.class))
                   .flatMap(tuple -> Mono.fromFuture(userService.setWorks(tuple.getT1(), tuple.getT2().getWorks())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> setLinks(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .zipWith(serverRequest.bodyToMono(Profile.class))
                   .flatMap(tuple -> Mono.fromFuture(userService.setLinks(tuple.getT1(), tuple.getT2().getLinks())))
                   .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return Mono.deferContextual(contextView -> Mono.just((Long) contextView.get(ContextNameRegistry.USER_ID)))
                   .flatMap(userId -> Mono.fromFuture(userService.getUser(userId)))
                   .flatMap(user -> {
                       return ServerResponse.ok().body(BodyInserters.fromValue(createProfile(user)));
                   });
    }

    private Profile createProfile(User user) {
        Profile.ProfileBuilder builder = Profile.builder();
        builder.status(user.getStatus());
        builder.aboutMe(user.getAboutMe());

        List<Education> educations = new ArrayList<>();
        user.getEducations()
            .forEach(education -> {
                educations.add(Education.builder()
                                        .degree(education.getDegree())
                                        .major(education.getMajor())
                                        .graduated(education.isGraduated())
                                        .build());
            });
        builder.educations(educations);

        List<Link> links = new ArrayList<>();
        user.getLinks()
            .forEach(link -> {
                links.add(Link.builder()
                              .name(link.getName())
                              .link(link.getLink())
                              .description(link.getDescription())
                              .build());
            });
        builder.links(links);

        List<Work> works = new ArrayList<>();
        user.getWorks()
            .forEach(work -> {
                works.add(Work.builder()
                              .role(work.getRole())
                              .technology(work.getTechnology())
                              .years(work.getYears())
                              .build());
            });
        builder.works(works);

        return builder.build();
    }
}
