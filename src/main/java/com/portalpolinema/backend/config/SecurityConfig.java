package com.portalpolinema.backend.config;

import com.portalpolinema.backend.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // aktifkan @PreAuthorize di controller
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Allow login & static

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error", "/favicon.ico").permitAll()

                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/img/**").permitAll()

                        // endpoint publik (hanya GET)
                        .requestMatchers(HttpMethod.GET, "/api/prodi/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/dosen/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mahasiswa/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/berita/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/kategori-jabatan/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/periode-jabatan/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/pengampu_jabatan/**").permitAll()

                        // Proteksi endpoint fitur X hanya Master

                        // Proteksi endpoint admin & master

                        .requestMatchers("/api/prodi/**").hasAnyRole("Admin", "Master")
                        .requestMatchers("/api/mahasiswa/**").hasAnyRole("Admin", "Master")
                        .requestMatchers("/api/dosen/**").hasAnyRole("Admin", "Master")
                        .requestMatchers("/api/kategori-jabatan/**").hasAnyRole("Admin", "Master")
                        .requestMatchers("/api/periode-jabatan/**").hasAnyRole("Admin", "Master")
                        .requestMatchers("/api/pengampu_jabatan/**").hasAnyRole("Admin", "Master")
                        // Selain itu, semua butuh login
                        .anyRequest().authenticated())
                // Stateless: gunakan token, bukan session
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Pasang filter JWT sebelum UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // temp di BackendApplication
    @Bean
    CommandLineRunner printHash(PasswordEncoder enc) {
        return args -> System.out.println(enc.encode("123"));
    }
}
