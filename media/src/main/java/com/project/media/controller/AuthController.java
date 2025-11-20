package com.project.media.controller;

import com.project.media.config.JwtUtil;
import com.project.media.dto.LoginUsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e geração de tokens JWT")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "Login Médico/Admin",
        description = "Login para profissionais de saúde\n\n" +
                     "Autentica médicos e administradores usando username e senha.\n\n" +
                     "Credenciais disponíveis estão documentadas no README do projeto."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                content = @Content)
    })
    @PostMapping("/login-profissional")
    public ResponseEntity<?> loginProfissional(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Tentativa de login para usuário: {}", loginRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            String token = jwtUtil.generateToken(userDetails.getUsername());
            
            logger.info("Login realizado com sucesso para usuário: {}", loginRequest.getUsername());
            
            return ResponseEntity.ok(new JwtResponse(
                token, 
                userDetails.getUsername(),
                userDetails.getAuthorities().toString(),
                LocalDateTime.now().plusDays(1)
            ));

        } catch (BadCredentialsException e) {
            logger.warn("Tentativa de login falhada para usuário: {} - Credenciais inválidas", 
                       loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthError("Usuário ou senha inválidos"));
                    
        } catch (Exception e) {
            logger.error("Erro interno durante login para usuário: {} - {}", 
                        loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthError("Erro interno do servidor"));
        }
    }

    @Operation(
        summary = "Login Usuário Comum",
        description = "Login para pacientes/usuários comuns\n\n" +
                     "Autentica usuários comuns usando apenas nome e CPF.\n\n" +
                     "Como usar:\n" +
                     "- Digite seu nome completo\n" +
                     "- Digite seu CPF (apenas números, 11 dígitos)\n" +
                     "- Exemplo CPF: 12345678901\n\n" +
                     "Importante: Seus dados são usados apenas para identificação e ficam armazenados no histórico de consultas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nome ou CPF inválidos",
                content = @Content)
    })
    @PostMapping("/login-usuario")
    public ResponseEntity<?> loginUsuario(@RequestBody @jakarta.validation.Valid LoginUsuarioDto loginUsuario) {
        try {
            logger.info("Tentativa de login para usuário comum: {}", loginUsuario);

            if (!isValidCPF(loginUsuario.getCpf())) {
                logger.warn("CPF inválido fornecido: {}", loginUsuario);
                return ResponseEntity.badRequest()
                        .body(new AuthError("CPF deve conter exatamente 11 dígitos"));
            }

            String token = jwtUtil.generateToken(loginUsuario.getNome() + "|" + loginUsuario.getCpf());
            
            logger.info("Login realizado com sucesso para usuário comum: {}", loginUsuario.getNome());
            
            return ResponseEntity.ok(new JwtResponse(
                token, 
                loginUsuario.getNome(),
                "USUARIO_COMUM",
                LocalDateTime.now().plusDays(1)
            ));

        } catch (Exception e) {
            logger.error("Erro interno durante login para usuário comum: {} - {}", 
                        loginUsuario.getNome(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthError("Erro interno do servidor"));
        }
    }

    private boolean isValidCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }

    @Operation(
        summary = "Renovar token",
        description = "Renova um token JWT válido"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "400", description = "Token não fornecido")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(new AuthError("Token não fornecido ou formato inválido"));
            }

            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String newToken = jwtUtil.generateToken(username);
                
                logger.info("Token renovado com sucesso para usuário: {}", username);
                
                return ResponseEntity.ok(new JwtResponse(
                    newToken,
                    username,
                    "USER",
                    LocalDateTime.now().plusDays(1)
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthError("Token inválido ou expirado"));
            }

        } catch (Exception e) {
            logger.error("Erro ao renovar token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthError("Erro interno do servidor"));
        }
    }

    @Operation(
        summary = "Validar token",
        description = "Verifica se um token JWT é válido"
    )
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                        .body(new ValidationResponse(false, "Token não fornecido"));
            }

            String token = authHeader.substring(7);
            boolean isValid = jwtUtil.validateToken(token);
            
            if (isValid) {
                String username = jwtUtil.getUsernameFromToken(token);
                return ResponseEntity.ok(new ValidationResponse(true, "Token válido", username));
            } else {
                return ResponseEntity.ok(new ValidationResponse(false, "Token inválido"));
            }

        } catch (Exception e) {
            logger.error("Erro ao validar token: {}", e.getMessage());
            return ResponseEntity.ok(new ValidationResponse(false, "Erro ao validar token"));
        }
    }

    public static class LoginRequest {
        @NotBlank(message = "Username é obrigatório")
        private String username;
        
        @NotBlank(message = "Password é obrigatório")
        private String password;

        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class JwtResponse {
        private String token;
        private String username;
        private String authorities;
        private LocalDateTime expiresAt;

        public JwtResponse(String token, String username, String authorities, LocalDateTime expiresAt) {
            this.token = token;
            this.username = username;
            this.authorities = authorities;
            this.expiresAt = expiresAt;
        }

        public String getToken() { return token; }
        public String getUsername() { return username; }
        public String getAuthorities() { return authorities; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
    }

    public static class AuthError {
        private String error;
        private LocalDateTime timestamp;

        public AuthError(String error) {
            this.error = error;
            this.timestamp = LocalDateTime.now();
        }

        public String getError() { return error; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    public static class ValidationResponse {
        private boolean valid;
        private String message;
        private String username;

        public ValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public ValidationResponse(boolean valid, String message, String username) {
            this.valid = valid;
            this.message = message;
            this.username = username;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public String getUsername() { return username; }
    }
}
