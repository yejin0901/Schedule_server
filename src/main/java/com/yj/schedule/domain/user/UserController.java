package com.yj.schedule.domain.user;


import com.yj.schedule.domain.login.SignupRequestDto;
import com.yj.schedule.global.CommonResponse;
import com.yj.schedule.global.jwt.JwtUtil;
import com.yj.schedule.global.security.UserDetailsImpl;
import com.yj.schedule.domain.login.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.yj.schedule.global.jwt.JwtUtil.AUTHORIZATION_HEADER;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtutil;

    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse<Object>> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }
        if (!userService.signup(requestDto)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                        .body(CommonResponse.builder()
                                .msg("중복된 username 입니다.")
                                .build());
        }
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CommonResponse.builder()
                        .msg("회원가입이 완료되었습니다.")
                        .build());
    }

    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        validateExistHeader(request);
        Long memberId = userDetails.getUser().getId();
        String refreshToken = JwtUtil.extractRefreshToken(request);
        refreshTokenService.matches(refreshToken, memberId);
        String accessToken = jwtutil.createAccessToken(userDetails.getUser().getUsername(),null);
        return ResponseEntity.noContent()
                .header(AUTHORIZATION_HEADER, "Bearer " + accessToken)
                .build();
    }

    private void validateExistHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String refreshTokenHeader = request.getHeader("Refresh-Token");
        if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
            throw new IllegalCallerException();
        }
    }



}
