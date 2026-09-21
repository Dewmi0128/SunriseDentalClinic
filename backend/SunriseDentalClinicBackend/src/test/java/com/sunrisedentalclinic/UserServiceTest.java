package com.sunrisedentalclinic;

import com.sunrisedentalclinic.model.User;
import com.sunrisedentalclinic.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    void testSuccessfulAuthentication() {

        User user = userService.authenticate("admin", "admin123");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("STAFF", user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void testIncorrectPassword() {

        User user = userService.authenticate("admin", "wrongpassword");

        assertNull(user);
    }

    @Test
    void testUnknownUsername() {

        User user = userService.authenticate(
                "unknownuser",
                "admin123"
        );

        assertNull(user);
    }

    @Test
    void testEmptyUsername() {

        User user = userService.authenticate(
                "",
                "admin123"
        );

        assertNull(user);
    }

    @Test
    void testEmptyPassword() {

        User user = userService.authenticate(
                "admin",
                ""
        );

        assertNull(user);
    }
}