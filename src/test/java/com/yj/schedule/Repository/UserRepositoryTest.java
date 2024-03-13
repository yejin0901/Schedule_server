package com.yj.schedule.Repository;

import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.user.UserRoleEnum;
import com.yj.schedule.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Given
        String username = "test123";
        String password = "password123";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(UserRoleEnum.USER);
        userRepository.save(user);

        // When
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // Then
        assertTrue(optionalUser.isPresent());
        User foundUser = optionalUser.get();
        assertEquals(username, foundUser.getUsername());
        assertEquals(password, foundUser.getPassword());
    }
}
