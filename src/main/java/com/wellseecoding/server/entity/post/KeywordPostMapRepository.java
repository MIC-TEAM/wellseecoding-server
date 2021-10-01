package com.wellseecoding.server.entity.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordPostMapRepository extends JpaRepository<KeywordPostMap, Long> {
    List<KeywordPostMap> findAllByPostId(Long postId);
}
