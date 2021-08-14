package com.wellseecoding.server.user.sns;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsInfoRepository extends JpaRepository<SnsInfo, SnsInfoKey> {
}
