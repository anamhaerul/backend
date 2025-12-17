package com.portalpolinema.backend.auth;

import com.portalpolinema.backend.user.User;
import com.portalpolinema.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Endpoint login (public)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Autentikasi email + password
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var principal = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        // Ambil data user dari DB
        User u = userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Buat token dengan claim role & id_user
        String token = jwtService.generateToken(
                u.getEmail(),
                Map.of("role", u.getRole().name(), "id_user", u.getId_user()));
        return ResponseEntity.ok(new LoginResponse(token, u.getId_user(), u.getEmail(), u.getRole()));
    }

    // Opsional: cek profil dari token
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractSubject(token);
        User u = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new LoginResponse(token, u.getId_user(), u.getEmail(), u.getRole()));
    }
}
