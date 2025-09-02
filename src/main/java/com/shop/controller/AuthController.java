package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.User;
import com.shop.security.JwtUtil;
import com.shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002"}, allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            UserDto user = userService.registerUser(registerRequest);
            String token = jwtUtil.generateToken(user.getEmail(), user.getId());
            
            AuthResponse response = new AuthResponse(true, "User registered successfully", token, user);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            String errorCode = e.getMessage().contains("already exists") ? "EMAIL_EXISTS" : "REGISTRATION_ERROR";
            AuthResponse response = new AuthResponse(false, e.getMessage(), errorCode);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Internal server error", "SERVER_ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());
            
            if (userOptional.isEmpty()) {
                AuthResponse response = new AuthResponse(false, "Invalid credentials", "INVALID_CREDENTIALS");
                return ResponseEntity.status(401).body(response);
            }
            
            User user = userOptional.get();
            
            if (!userService.validatePassword(loginRequest.getPassword(), user.getPasswordHash())) {
                AuthResponse response = new AuthResponse(false, "Invalid credentials", "INVALID_CREDENTIALS");
                return ResponseEntity.status(401).body(response);
            }
            
            String token = jwtUtil.generateToken(user.getEmail(), user.getId());
            UserDto userDto = userService.getUserByEmail(user.getEmail());
            
            AuthResponse response = new AuthResponse(true, "Login successful", token, userDto);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Internal server error", "SERVER_ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            UserDto user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(404).body(new AuthResponse(false, "User not found", "USER_NOT_FOUND"));
            }
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse(false, "Internal server error", "SERVER_ERROR"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UserDto userDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            UserDto updatedUser = userService.updateUser(email, userDto);
            return ResponseEntity.ok(updatedUser);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new AuthResponse(false, e.getMessage(), "USER_NOT_FOUND"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse(false, "Internal server error", "SERVER_ERROR"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        // In a stateless JWT implementation, logout is handled client-side by removing the token
        // You could implement a token blacklist here if needed
        AuthResponse response = new AuthResponse(true, "Logout successful");
        return ResponseEntity.ok(response);
    }
} 