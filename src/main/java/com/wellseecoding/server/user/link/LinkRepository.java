package com.wellseecoding.server.user.link;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByUserId(Long userId);
}
