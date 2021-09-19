package com.wellseecoding.server.entity.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagPostMapRepository extends JpaRepository<TagPostMap, Long> {
    List<TagPostMap> findAllByPostId(Long postId);
}
