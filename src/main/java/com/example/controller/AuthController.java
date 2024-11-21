package com.example.controller;

import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Đăng ký tài khoản
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        userService.registerUser(email, password);
        return ResponseEntity.ok("Registration successful! Check your email for OTP.");
    }

    // Xác thực OTP
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String otp) {
        userService.verifyUser(email, otp);
        return ResponseEntity.ok("Verification successful!");
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean isLoggedIn = userService.login(email, password);
        if (isLoggedIn) {
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
