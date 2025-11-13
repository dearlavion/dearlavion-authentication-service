package com.dearlavion.auth.controller;

import com.dearlavion.auth.model.User;
import com.dearlavion.auth.model.UserVO;
import com.dearlavion.auth.service.JwtService;
import com.dearlavion.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserVO registerRequest) {
        try {
            var user = userService.registerUser(registerRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "user", user.getUsername()
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(Map.of(
                    "error", "User already exists"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Something went wrong"
            ));
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try {
            User user = (User) userService.loadUserByUsername(username);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            // ✅ Convert to UserVO (avoid exposing password)
            UserVO userVO = new UserVO();
            userVO.setUsername(user.getUsername());
            userVO.setEmail(user.getEmail());
            userVO.setFirstname(user.getFirstname());
            userVO.setLastname(user.getLastname());
            userVO.setPhone(user.getPhone());
            userVO.setImage(user.getImage());

            return ResponseEntity.ok(userVO);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserVO loginRequest) {
        // ✅ Load the user by username or email (depending on your logic)
        Optional<UserDetails> user = Optional.ofNullable(userService.loadUserByUsername(loginRequest.getUsername()));

        if (user.isEmpty()) {
            user = Optional.ofNullable(userService.loadUserByEmail(loginRequest.getEmail()));
        }

        // ✅ Validate password
        if (user.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        // ✅ Generate JWT
        String token = jwtService.generateToken(user.get());

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
