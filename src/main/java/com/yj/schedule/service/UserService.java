package com.yj.schedule.service;


import com.yj.schedule.dto.CommonResponse;
import com.yj.schedule.dto.SignupRequestDto;
import com.yj.schedule.entity.User;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public Boolean signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            return false;
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
        return true;
    }
}
