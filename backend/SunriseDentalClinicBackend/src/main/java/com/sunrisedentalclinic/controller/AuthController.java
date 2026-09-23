package com.sunrisedentalclinic.controller;

import com.sunrisedentalclinic.model.User;
import com.sunrisedentalclinic.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController() {
        this.userService = new UserService();
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (user == null) {
            return "Invalid username or password";
        }

        return "Login successful. Welcome " + user.getUsername();
    }
}