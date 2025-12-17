package com.portalpolinema.backend.auth;

import com.portalpolinema.backend.user.User;
import com.portalpolinema.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ambil user by email, jika tidak ada lempar 404 auth
        User us = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Konversi role ke authority Spring (ROLE_<NamaRole>)
        return new org.springframework.security.core.userdetails.User(
                us.getEmail(),
                us.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + us.getRole().name())));
    }
}
