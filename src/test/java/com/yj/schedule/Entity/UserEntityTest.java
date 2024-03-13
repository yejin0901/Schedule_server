package com.yj.schedule.Entity;

import com.yj.schedule.domain.user.User;
import com.yj.schedule.domain.user.UserRoleEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserEntityTest {
    @Test
    public void testUserValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        User user = new User("inv@", "pass", UserRoleEnum.USER);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.size(), "Expected 2 violations");

        for (ConstraintViolation<User> violation : violations) {
            switch (violation.getPropertyPath().toString()) {
                case "username":
                    assertEquals("\"^[a-z0-9]{4,10}$\"와 일치해야 합니다", violation.getMessage());
                    break;
                case "password":
                    assertEquals("\"^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$\"와 일치해야 합니다", violation.getMessage());
                    break;
                default:
                    throw new AssertionError("Unexpected violation: " + violation.getPropertyPath());
            }
        }
    }

}