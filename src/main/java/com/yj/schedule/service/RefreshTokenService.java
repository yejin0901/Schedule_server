package com.yj.schedule.service;

import com.yj.schedule.entity.RefreshToken;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void matches(String refreshToken, Long memberId) {
        RefreshToken savedToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);
        savedToken.validateSameToken(refreshToken);
    }


    public void saveToken(String refreshtoken, Long id) {
        refreshTokenRepository.save(new RefreshToken(id, refreshtoken));
    }
}
