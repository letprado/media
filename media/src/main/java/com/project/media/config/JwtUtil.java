package com.project.media.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            logger.warn("Token JWT expirado: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.warn("Token JWT não suportado: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.warn("Token JWT malformado: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("Token JWT vazio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(String token) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            String username = claims.getSubject();
            Map<String, Object> newClaims = new HashMap<>(claims);
            
            return createToken(newClaims, username);
        } catch (Exception e) {
            logger.error("Erro ao renovar token: {}", e.getMessage());
            throw new RuntimeException("Não é possível renovar o token", e);
        }
    }
}
