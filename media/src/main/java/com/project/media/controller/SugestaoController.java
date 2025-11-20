package com.project.media.controller;

import com.project.media.dto.HistoricoConsultaDto;
import com.project.media.dto.SugestaoRequestDto;
import com.project.media.dto.SugestaoResponseDto;
import com.project.media.service.SugestaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sugestoes")
@Tag(name = "Sugestões Médicas", description = "API para obter sugestões médicas baseadas em sintomas")
public class SugestaoController {

    private static final Logger logger = LoggerFactory.getLogger(SugestaoController.class);

    @Autowired
    private SugestaoService sugestaoService;

    @Operation(
        summary = "Obter Sugestões Médicas",
        description = "Sistema de Pré-Triagem Digital MedIA\n\n" +
                     "Informe seus sintomas e receba orientações médicas personalizadas baseadas em nosso banco de conhecimento.\n\n" +
                     "Como usar:\n" +
                     "1. Faça login primeiro (usuário comum ou profissional)\n" +
                     "2. Liste seus sintomas (ex: febre, tosse, dor de cabeça)\n" +
                     "3. Receba sugestões de atendimento priorizadas\n\n" +
                     "Tipos de atendimento retornados:\n" +
                     "- URGENTE - Procure pronto-socorro imediatamente\n" +
                     "- CONSULTA - Agende consulta médica\n" +
                     "- OBSERVACAO - Monitore sintomas em casa\n" +
                     "- AUTOCUIDADO - Tratamento caseiro\n\n" +
                     "Sintomas disponíveis: febre, tosse, dor de cabeça, dor abdominal, náusea, dor no peito, falta de ar, palpitação, tontura, fadiga e muitos outros."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugestões encontradas com sucesso",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = SugestaoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<List<SugestaoResponseDto>> obterSugestoes(
            @Valid @RequestBody SugestaoRequestDto request,
            HttpServletRequest httpRequest) {
        
        try {
            logger.info("Recebida requisição para obter sugestões: {}", request);
            
            List<SugestaoResponseDto> sugestoes = sugestaoService.buscarSugestoes(request, httpRequest);
            
            logger.info("Retornando {} sugestões", sugestoes.size());
            return ResponseEntity.ok(sugestoes);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Dados de entrada inválidos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            logger.error("Erro ao processar requisição: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Histórico de Consultas",
        description = "Acesso ao histórico de consultas realizadas (Apenas Médicos e Administradores)\n\n" +
                     "Visualize todas as consultas realizadas no sistema com filtros por CPF e período.\n\n" +
                     "Funcionalidades:\n" +
                     "- Paginação automática (padrão: 10 itens por página)\n" +
                     "- Filtro por CPF do paciente\n" +
                     "- Filtro por período (data início e data fim)\n" +
                     "- Ordenação por data (padrão: mais recentes primeiro)\n\n" +
                     "Filtros disponíveis:\n" +
                     "- cpfPaciente: CPF do paciente para buscar histórico específico (opcional)\n" +
                     "- dataInicio: Data de início do período (formato: yyyy-MM-ddTHH:mm:ss)\n" +
                     "- dataFim: Data de fim do período (formato: yyyy-MM-ddTHH:mm:ss)\n\n" +
                     "Exemplo: /api/sugestoes/history?cpfPaciente=12345678901&dataInicio=2025-11-01T00:00:00&dataFim=2025-11-30T23:59:59"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/history")
    public ResponseEntity<Page<HistoricoConsultaDto>> obterHistorico(
            @Parameter(description = "Número da página (começando em 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamanho da página (máximo 50)")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo para ordenação (dataConsulta, prioridade, etc.)")
            @RequestParam(defaultValue = "dataConsulta") String sort,
            
            @Parameter(description = "Direção da ordenação (ASC ou DESC)")
            @RequestParam(defaultValue = "desc") String direction,
            
            @Parameter(description = "CPF do paciente para filtrar (11 dígitos, sem formatação)")
            @RequestParam(required = false) String cpfPaciente,
            
            @Parameter(description = "Data de início do período (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            
            @Parameter(description = "Data de fim do período (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        
        try {
            logger.info("Buscando histórico - Página: {}, Tamanho: {}, CPF: {}, Período: {} a {}", 
                       page, size, cpfPaciente, dataInicio, dataFim);

            if (page < 0) {
                logger.warn("Número da página inválido: {}", page);
                return ResponseEntity.badRequest().build();
            }
            
            if (size <= 0 || size > 50) {
                logger.warn("Tamanho da página inválido: {}", size);
                return ResponseEntity.badRequest().build();
            }

            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") 
                    ? Sort.Direction.ASC : Sort.Direction.DESC;
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

            Page<HistoricoConsultaDto> historico = sugestaoService.buscarHistorico(
                    cpfPaciente, dataInicio, dataFim, pageable);

            logger.info("Histórico recuperado - Total de elementos: {}, Páginas: {}", 
                       historico.getTotalElements(), historico.getTotalPages());

            return ResponseEntity.ok(historico);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar histórico: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(
        summary = "Health Check",
        description = "Verificação de status da API\n\n" +
                     "Este endpoint verifica se a aplicação está funcionando corretamente.\n\n" +
                     "Para que serve:\n" +
                     "- Confirmar que a API está online\n" +
                     "- Testar conectividade sem precisar de autenticação\n" +
                     "- Diagnóstico rápido de problemas\n" +
                     "- Monitoramento automático da aplicação\n\n" +
                     "Uso típico: Sistemas de monitoramento fazem chamadas automáticas para garantir que o serviço está ativo."
    )
    @GetMapping("/health")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok(new Object() {
            @SuppressWarnings("unused")
            public final String status = "UP";
            @SuppressWarnings("unused")
            public final String timestamp = LocalDateTime.now().toString();
            @SuppressWarnings("unused")
            public final String service = "MedIA API";
        });
    }
}
