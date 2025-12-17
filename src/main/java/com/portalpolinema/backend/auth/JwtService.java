package com.portalpolinema.backend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

@Service
public class JwtService {
    // Secret base64 diambil dari properties
    @Value("${app.jwt.secret}")
    private String secret;

    // Lama hidup token (ms)
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // Konversi secret base64 ke Key HMAC
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // Buat token dengan subject (email) + claims tambahan (role, id_user, dsb)
    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key(), Jwts.SIG.HS256) // tanda tangani token HMAC-SHA256 (API baru)
                .compact();
    }

    // Ambil subject (email) dari token
    public String extractSubject(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    // Validasi signature + expiry
    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token); // verifikasi tanda tangan + exp
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
