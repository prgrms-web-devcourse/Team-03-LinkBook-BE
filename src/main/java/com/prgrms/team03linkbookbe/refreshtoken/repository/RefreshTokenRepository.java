package com.prgrms.team03linkbookbe.refreshtoken.repository;

import com.prgrms.team03linkbookbe.refreshtoken.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);
}
