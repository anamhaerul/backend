package com.portalpolinema.backend.auth;

import com.portalpolinema.backend.user.User;

// Response login dikirim ke frontend
public record LoginResponse(String token, Integer id_user, String email, User.Role role) {
}
