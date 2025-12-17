package com.portalpolinema.backend.auth;

// Payload login (email + password plain dari user)
public record LoginRequest(String email, String password) {
}
