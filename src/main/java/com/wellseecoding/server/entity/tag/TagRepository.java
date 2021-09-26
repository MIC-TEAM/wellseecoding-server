package com.wellseecoding.server.entity.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    public static final String GET_RANDOM_ROWS = "select * from tags where rand() limit :limit";

    @Query(value = GET_RANDOM_ROWS, nativeQuery = true)
    List<Tag> getRandomTags(@Param("limit") int limit);

    Optional<Tag> findByValue(String value);
}
