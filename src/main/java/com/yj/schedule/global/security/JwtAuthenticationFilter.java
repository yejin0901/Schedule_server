package com.yj.schedule.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yj.schedule.domain.login.LoginRequestDto;
import com.yj.schedule.domain.user.UserRoleEnum;
import com.yj.schedule.jwt.JwtUtil;
import com.yj.schedule.domain.login.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인 및 JWT 생성")

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/api/user/login");
    }
//    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
//        this.jwtUtil = jwtUtil;
//        this.refreshTokenService = refreshTokenService;
//        setFilterProcessesUrl("/api/user/login");
//    }

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
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        if (authResult.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
            String username = userDetails.getUsername();
            UserRoleEnum role = userDetails.getUser().getRole();

            String accessToken = jwtUtil.createAccessToken(username, role);
            String refreshToken = jwtUtil.createRefreshToken(username);
            refreshTokenService.saveToken(refreshToken, userDetails.getUser().getId());

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
            response.addHeader("Cache-Control", "no-store"); // Add Cache-Control header
            response.setStatus(HttpServletResponse.SC_OK); // Set status code before getting PrintWriter

            PrintWriter writer = response.getWriter();
            writer.println("로그인이 완료되었습니다."); // Print response body
            writer.flush(); // Flush the writer to ensure the message is sent
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter writer = response.getWriter();
            writer.println("사용자 정보를 찾을 수 없습니다."); // Print response body
            writer.flush(); // Flush the writer to ensure the message is sent
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("회원을 찾을 수 없습니다.");
    }


}