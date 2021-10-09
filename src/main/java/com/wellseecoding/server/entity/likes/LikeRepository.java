package com.wellseecoding.server.entity.likes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findAllByLikeIdUserId(Long userId);
}
