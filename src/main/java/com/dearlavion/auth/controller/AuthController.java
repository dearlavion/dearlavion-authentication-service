package com.dearlavion.auth.controller;

import com.dearlavion.auth.service.JwtService;
import com.dearlavion.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        var user = userService.registerUser(req.get("username"), req.get("password"));
        return ResponseEntity.ok(Map.of("message", "User registered", "user", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        var user = userService.loadUserByUsername(req.get("username"));
        if (!passwordEncoder.matches(req.get("password"), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    /*@PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");

        boolean changed = userService.changePassword(username, oldPassword, newPassword);
        if (!changed) {
            return ResponseEntity.status(400).body(Map.of("error", "Old password is incorrect"));
        }
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }*/

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(Map.of("message", "Authenticated successfully!"));
    }
}
