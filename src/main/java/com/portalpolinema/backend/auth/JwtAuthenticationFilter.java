package com.portalpolinema.backend.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        // Lewati pemeriksaan token untuk endpoint publik (GET)
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String uri = request.getRequestURI();
            if (uri.startsWith("/api/mahasiswa")
                    || uri.startsWith("/api/prodi")
                    || uri.startsWith("/api/dosen")
                    || uri.startsWith("/api/berita")
                    || uri.startsWith("/api/mahasiswa")
                    || uri.startsWith("/api/kategori-jabatan")
                    || uri.startsWith("/api/periode-jabatan")
                    || uri.startsWith("/api/pengampu_jabatan")) {
                chain.doFilter(request, response);
                return;
            }
        }
        // Ambil header Authorization
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Jika tidak ada Bearer token, lewati
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        try {
            // Validasi token; jika tidak valid, balas 401 dengan pesan
            if (!jwtService.isValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"message\":\"token bermasalah\"}");
                return;
            }

            String email = jwtService.extractSubject(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            // Set autentikasi di SecurityContext
            var auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("JWT error for {}: {} - {}", request.getRequestURI(),
                    ex.getClass().getSimpleName(), ex.getMessage());
            log.error("Header Authorization: {}", header);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"token bermasalah\"}");
        }
    }
}
