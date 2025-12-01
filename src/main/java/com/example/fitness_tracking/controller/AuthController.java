package com.example.fitness_tracking.controller;

import com.example.fitness_tracking.config.JWTUtil;
import com.example.fitness_tracking.config.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API для аутентифікації та отримання JWT токенів")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    @Operation(
        summary = "Отримати JWT токен",
        responses = {
            @ApiResponse(responseCode = "200", description = "Успішна автентифікація",
                content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Невірний логін/пароль")
        }
    )
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String role = userDetails.getAuthorities().iterator().next().getAuthority();
        final String jwt = jwtUtil.generateToken(authRequest.getUsername(), role);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("role", role);
        response.put("username", authRequest.getUsername());

        return ResponseEntity.ok(response);
    }

    public static class AuthRequest {
        @Schema(description = "Ім'я користувача", example = "admin")
        private String username;
        
        @Schema(description = "Пароль", example = "admin123")
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}