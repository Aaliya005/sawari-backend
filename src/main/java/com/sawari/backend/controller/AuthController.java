package com.sawari.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sawari.backend.dto.LoginRequest;
import com.sawari.backend.dto.LoginResponse;
import com.sawari.backend.dto.RegisterRequest;
import com.sawari.backend.entity.User;
import com.sawari.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // plain — service will hash it
        user.setRole(request.getRole());
        user.setVerified(false);

        User savedUser = userService.register(user);
        savedUser.setPassword(null); // never send hash to frontend

        return ResponseEntity.ok(savedUser);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request);

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    "Login successful",
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isVerified()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}