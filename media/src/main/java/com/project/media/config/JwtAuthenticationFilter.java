package com.project.media.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain chain) throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwtToken = null;
        
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(jwtToken);
                logger.debug("Usuário extraído do token: {}", username);
            } catch (Exception e) {
                logger.warn("Não foi possível obter username do token JWT: {}", e.getMessage());
            }
        } else {
            logger.debug("JWT Token não encontrado ou não inicia com Bearer");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (jwtUtil.validateToken(jwtToken)) {
                logger.debug("Token JWT válido para usuário: {}", username);
                
                List<SimpleGrantedAuthority> authorities;
                if (username.contains("|")) {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                } else {
                    authorities = determineAuthorities(username);
                }
                
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        username, 
                        null, 
                        authorities
                    );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                logger.debug("Usuário {} autenticado com sucesso", username);
            } else {
                logger.warn("Token JWT inválido para usuário: {}", username);
            }
        }

        chain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> determineAuthorities(String username) {
        switch (username.toLowerCase()) {
            case "admin":
                return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
                );
            case "medico":
                return List.of(
                    new SimpleGrantedAuthority("ROLE_MEDICO"),
                    new SimpleGrantedAuthority("ROLE_USER")
                );
            default:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        return path.startsWith("/api/auth/") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.equals("/api/sugestoes/health") ||
               path.startsWith("/h2-console") ||
               path.startsWith("/actuator");
    }
}
