package com.yj.schedule.Service;

import com.yj.schedule.dto.SignupRequestDto;
import com.yj.schedule.entity.User;
import com.yj.schedule.entity.UserRoleEnum;
import com.yj.schedule.repository.UserRepository;
import com.yj.schedule.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void testSignup_Successful() {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("user1234");
        requestDto.setPassword("pwpw1234");
        String encodedPassword = "encodedPassword";

        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn(encodedPassword);
        UserService userService = new UserService(userRepository,passwordEncoder);

        // When
        Boolean result = userService.signup(requestDto);

        // Then
        assertTrue(result);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
        assertEquals(requestDto.getUsername(), savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(UserRoleEnum.USER, savedUser.getRole());
    }

    @Test
    public void testSignup_Failure_DuplicateUsername() {
        // Given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("existingUser");
        requestDto.setPassword("existingPassword");
        User existingUser = new User("existingUser", "existingPassword", UserRoleEnum.USER);

        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(existingUser));
        UserService userService = new UserService(userRepository,passwordEncoder);

        // When
        Boolean result = userService.signup(requestDto);

        // Then
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
}
