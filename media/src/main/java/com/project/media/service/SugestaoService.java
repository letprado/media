package com.project.media.service;

import com.project.media.dto.HistoricoConsultaDto;
import com.project.media.dto.SugestaoRequestDto;
import com.project.media.dto.SugestaoResponseDto;
import com.project.media.entity.HistoricoConsulta;
import com.project.media.entity.Sintoma;
import com.project.media.entity.Sugestao;
import com.project.media.repository.HistoricoConsultaRepository;
import com.project.media.repository.SintomaRepository;
import com.project.media.repository.SugestaoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SugestaoService {

    private static final Logger logger = LoggerFactory.getLogger(SugestaoService.class);

    @Autowired
    private SugestaoRepository sugestaoRepository;

    @Autowired
    private SintomaRepository sintomaRepository;

    @Autowired
    private HistoricoConsultaRepository historicoRepository;

    @Transactional(timeout = 5, readOnly = true)
    public List<SugestaoResponseDto> buscarSugestoes(SugestaoRequestDto request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        
        try {
            logger.info("Iniciando busca de sugestões para sintomas: {}", request.getSintomas());

            List<String> sintomasNormalizados = request.getSintomas().stream()
                    .map(s -> s.toLowerCase().trim())
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            if (sintomasNormalizados.isEmpty()) {
                throw new IllegalArgumentException("Lista de sintomas não pode estar vazia");
            }

            List<Sintoma> sintomasEncontrados = sintomaRepository.findByNomesContaining(sintomasNormalizados);
            logger.info("Encontrados {} sintomas no banco de dados", sintomasEncontrados.size());

            List<SugestaoResponseDto> sugestoes;
            
            if (sintomasEncontrados.isEmpty()) {
                sugestoes = criarSugestaoGenerica(request.getSintomas());
            } else {
                List<String> nomesSintomasEncontrados = sintomasEncontrados.stream()
                        .map(s -> s.getNome().toLowerCase())
                        .collect(Collectors.toList());

                List<Object[]> resultados = sugestaoRepository.findMelhoresSugestoesPorSintomas(nomesSintomasEncontrados);
                
                sugestoes = resultados.stream()
                        .map(resultado -> {
                            Sugestao sugestao = (Sugestao) resultado[0];
                            
                            List<String> sintomasCorrespondentes = sugestao.getSintomas().stream()
                                    .filter(s -> nomesSintomasEncontrados.contains(s.getNome().toLowerCase()))
                                    .map(Sintoma::getNome)
                                    .collect(Collectors.toList());

                            return new SugestaoResponseDto(
                                    sugestao.getId(),
                                    sugestao.getTitulo(),
                                    sugestao.getDescricao(),
                                    sugestao.getTipoAtendimento(),
                                    sugestao.getPrioridade(),
                                    sugestao.getRecomendacoes(),
                                    sintomasCorrespondentes
                            );
                        })
                        .limit(5)
                        .collect(Collectors.toList());
            }

            if (!sugestoes.isEmpty()) {
                registrarHistorico(request.getSintomas(), sugestoes.get(0), httpRequest, startTime);
            }

            logger.info("Busca concluída. Retornando {} sugestões", sugestoes.size());
            return sugestoes;

        } catch (Exception e) {
            logger.error("Erro ao buscar sugestões: {}", e.getMessage(), e);
            throw new RuntimeException("Erro interno do servidor", e);
        }
    }

    private List<SugestaoResponseDto> criarSugestaoGenerica(List<String> sintomas) {
        logger.info("Criando sugestão genérica para sintomas não encontrados: {}", sintomas);
        
        SugestaoResponseDto sugestaoGenerica = new SugestaoResponseDto(
                0L,
                "Consulta Médica Recomendada",
                "Com base nos sintomas informados, recomendamos que você procure atendimento médico " +
                "para uma avaliação mais detalhada. Um profissional de saúde poderá realizar " +
                "um diagnóstico adequado e indicar o melhor tratamento.",
                "CONSULTA",
                3,
                "• Procure uma unidade de saúde mais próxima\n" +
                "• Leve uma lista detalhada dos sintomas\n" +
                "• Informe sobre medicamentos que está tomando\n" +
                "• Em caso de urgência, procure o pronto-socorro",
                sintomas
        );

        return List.of(sugestaoGenerica);
    }

    @Transactional(timeout = 2)
    private void registrarHistorico(List<String> sintomas, SugestaoResponseDto sugestaoDto, 
                                   HttpServletRequest httpRequest, long startTime) {
        try {
            String sintomasString = String.join(", ", sintomas);
            long tempoResposta = System.currentTimeMillis() - startTime;

            HistoricoConsulta historico = new HistoricoConsulta();
            historico.setSintomasInformados(sintomasString);
            historico.setTempoRespostaMs(tempoResposta);
            historico.setDataConsulta(LocalDateTime.now());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName() != null) {
                String username = authentication.getName();
                if (username.contains("|")) {
                    String[] partes = username.split("\\|");
                    if (partes.length == 2) {
                        historico.setNomePaciente(partes[0]);
                        historico.setCpfPaciente(partes[1]);
                    }
                } else {
                    historico.setNomePaciente(username);
                }
            }

            if (httpRequest != null) {
                historico.setIpOrigem(obterIpOrigem(httpRequest));
                historico.setUserAgent(httpRequest.getHeader("User-Agent"));
            }

            if (sugestaoDto.getId() != null && sugestaoDto.getId() > 0) {
                sugestaoRepository.findById(sugestaoDto.getId())
                        .ifPresent(historico::setSugestao);
            }

            historicoRepository.save(historico);
            logger.info("Histórico registrado com sucesso. ID: {}, CPF: {}", historico.getId(), historico.getCpfPaciente());

        } catch (Exception e) {
            logger.error("Erro ao registrar histórico: {}", e.getMessage(), e);
        }
    }

    private String obterIpOrigem(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    @Transactional(timeout = 3, readOnly = true)
    public Page<HistoricoConsultaDto> buscarHistorico(String cpfPaciente,
                                                     LocalDateTime dataInicio, LocalDateTime dataFim,
                                                     Pageable pageable) {
        logger.info("Buscando histórico com filtros - CPF: {}, Período: {} a {}", 
                   cpfPaciente, dataInicio, dataFim);

        Page<HistoricoConsulta> historicos = historicoRepository.findComFiltros(
                cpfPaciente, dataInicio, dataFim, pageable);

        return historicos.map(this::converterParaDto);
    }

    private HistoricoConsultaDto converterParaDto(HistoricoConsulta historico) {
        HistoricoConsultaDto dto = new HistoricoConsultaDto();
        dto.setId(historico.getId());
        dto.setSintomasInformados(historico.getSintomasInformados());
        dto.setCpfPaciente(historico.getCpfPaciente());
        dto.setNomePaciente(historico.getNomePaciente());
        dto.setIpOrigem(historico.getIpOrigem());
        dto.setDataConsulta(historico.getDataConsulta());
        dto.setTempoRespostaMs(historico.getTempoRespostaMs());
        dto.setObservacoes(historico.getObservacoes());

        if (historico.getSugestao() != null) {
            dto.setTituloSugestao(historico.getSugestao().getTitulo());
            dto.setTipoAtendimento(historico.getSugestao().getTipoAtendimento());
            dto.setPrioridade(historico.getSugestao().getPrioridade());
        }

        return dto;
    }

}
