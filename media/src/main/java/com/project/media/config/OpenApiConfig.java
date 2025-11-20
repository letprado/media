package com.project.media.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MedIA API - Sistema de Pré-Triagem Digital")
                        .version("1.0.0")
                        .description("## **Bem-vindo ao MedIA!**\n\n" +
                                   "**Sistema inteligente de pré-triagem médica que otimiza o atendimento em unidades de saúde.**\n\n" +
                                   "---\n\n" +
                                   "### **O que fazemos:**\n" +
                                   "• **Análise inteligente** de sintomas informados pelo paciente\n" +
                                   "• **Orientações médicas** baseadas em conhecimento especializado\n" +
                                   "• **Priorização automática** por nível de urgência\n" +
                                   "• **Histórico completo** para profissionais de saúde\n\n" +
                                   "### **Como começar:**\n\n" +
                                   "#### **Para Pacientes/Usuários:**\n" +
                                   "1. Use `/api/auth/login-usuario` com **nome + CPF**\n" +
                                   "2. Copie o **token** retornado\n" +
                                   "3. Clique no **cadeado** ao lado do endpoint `/api/sugestoes`\n" +
                                   "4. Cole o token no formato: `Bearer SEU_TOKEN_AQUI`\n" +
                                   "5. Informe seus sintomas e receba orientações!\n\n" +
                                   "#### **Para Profissionais de Saúde:**\n" +
                                   "1. Use `/api/auth/login-profissional` com **username + senha**\n" +
                                   "2. Acesso completo a sugestões + histórico de consultas\n\n" +
                                   "### **Autenticação por Sessão:**\n" +
                                   "• Tokens JWT válidos por **24 horas**\n" +
                                   "• Use o **cadeado** em cada endpoint para autorizar\n" +
                                   "• Formato: `Bearer seu_token_jwt_aqui`\n\n" +
                                   "---\n\n" +
                                   "### **Teste rápido:**\n" +
                                   "• Comece com `/api/sugestoes/health` (sem autenticação)\n" +
                                   "• Experimente sintomas como: `[\"febre\", \"tosse\", \"dor de cabeça\"]`"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://media-api.cloud.com")
                                .description("Servidor de Produção")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtido através do endpoint de login. " +
                                                   "Formato: Bearer <token>")));
    }
}
