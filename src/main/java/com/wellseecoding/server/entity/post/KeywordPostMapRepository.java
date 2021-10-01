package com.wellseecoding.server.entity.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordPostMapRepository extends JpaRepository<KeywordPostMap, Long> {
    @Query(value = "select * from keyword_post_map where keyword LIKE :keyword%", nativeQuery = true)
    List<KeywordPostMap> search(@Param("keyword") String keyword);

    List<KeywordPostMap> findAllByPostId(Long postId);
}
