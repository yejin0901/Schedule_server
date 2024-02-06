package com.yj.schedule.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yj.schedule.dto.LoginRequestDto;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/api/user/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            log.info(request.getInputStream().toString());
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword(), null));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        log.info("로그인 성공");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accesstoken = jwtUtil.createAccessToken(username, role);
        String refreshtoken = jwtUtil.createRefreshToken(username);
        refreshTokenService.saveToken(refreshtoken, ((UserDetailsImpl) authResult.getPrincipal()).getUser().getId());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accesstoken);
        response.setStatus(HttpServletResponse.SC_OK); // 상태 코드 설정
        response.getWriter().println("로그인이 완료되었습니다."); // 응답 본문 작성
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("회원을 찾을 수 없습니다.");
    }


}