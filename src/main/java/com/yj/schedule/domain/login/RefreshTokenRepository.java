package com.yj.schedule.domain.login;

import com.yj.schedule.domain.login.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByMemberId(Long memberId);
}
