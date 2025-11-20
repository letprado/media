package com.project.media.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SugestaoResponseDto {

    private Long id;
    private String titulo;
    private String descricao;
    private String tipoAtendimento;
    private Integer prioridade;
    private String recomendacoes;
    private List<String> sintomasCorrespondentes;
    private LocalDateTime dataConsulta;

    public SugestaoResponseDto() {
    }

    public SugestaoResponseDto(Long id, String titulo, String descricao, String tipoAtendimento, 
                              Integer prioridade, String recomendacoes, List<String> sintomasCorrespondentes) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoAtendimento = tipoAtendimento;
        this.prioridade = prioridade;
        this.recomendacoes = recomendacoes;
        this.sintomasCorrespondentes = sintomasCorrespondentes;
        this.dataConsulta = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public String getRecomendacoes() {
        return recomendacoes;
    }

    public void setRecomendacoes(String recomendacoes) {
        this.recomendacoes = recomendacoes;
    }

    public List<String> getSintomasCorrespondentes() {
        return sintomasCorrespondentes;
    }

    public void setSintomasCorrespondentes(List<String> sintomasCorrespondentes) {
        this.sintomasCorrespondentes = sintomasCorrespondentes;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    @Override
    public String toString() {
        return "SugestaoResponseDto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipoAtendimento='" + tipoAtendimento + '\'' +
                ", prioridade=" + prioridade +
                ", dataConsulta=" + dataConsulta +
                '}';
    }
}

