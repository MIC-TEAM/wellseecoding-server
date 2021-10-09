package com.wellseecoding.server.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Post {
    private Long id;
    private Long userId;
    private String name;
    private String deadline;
    private String schedule;
    private String summary;
    private String qualification;
    private String size;
    private List<String> tags;
    private Long commentCount;

    public static Post fromEntity(com.wellseecoding.server.entity.post.Post entity, long commentCount) {
        return new Post(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getDeadline(),
                entity.getSchedule(),
                entity.getSummary(),
                entity.getQualification(),
                entity.getSize(),
                entity.getTagPostMaps()
                      .stream()
                      .map(tagPostMap -> tagPostMap.getTag().getValue())
                      .collect(Collectors.toList()),
                commentCount
        );
    }
}
