package com.wellseecoding.server.entity.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagUserMapRepository extends JpaRepository<TagUserMap, Long> {
    List<TagUserMap> findAllByUserId(long userId);
}
