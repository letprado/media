package com.project.media.dto;

import java.time.LocalDateTime;

public class HistoricoConsultaDto {

    private Long id;
    private String sintomasInformados;
    private String cpfPaciente;
    private String nomePaciente;
    private String tituloSugestao;
    private String tipoAtendimento;
    private Integer prioridade;
    private String ipOrigem;
    private LocalDateTime dataConsulta;
    private Long tempoRespostaMs;
    private String observacoes;

    public HistoricoConsultaDto() {
    }

    public HistoricoConsultaDto(Long id, String sintomasInformados, String tituloSugestao, 
                               String tipoAtendimento, Integer prioridade, String ipOrigem,
                               LocalDateTime dataConsulta, Long tempoRespostaMs) {
        this.id = id;
        this.sintomasInformados = sintomasInformados;
        this.tituloSugestao = tituloSugestao;
        this.tipoAtendimento = tipoAtendimento;
        this.prioridade = prioridade;
        this.ipOrigem = ipOrigem;
        this.dataConsulta = dataConsulta;
        this.tempoRespostaMs = tempoRespostaMs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSintomasInformados() {
        return sintomasInformados;
    }

    public void setSintomasInformados(String sintomasInformados) {
        this.sintomasInformados = sintomasInformados;
    }

    public String getTituloSugestao() {
        return tituloSugestao;
    }

    public void setTituloSugestao(String tituloSugestao) {
        this.tituloSugestao = tituloSugestao;
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

    public String getIpOrigem() {
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Long getTempoRespostaMs() {
        return tempoRespostaMs;
    }

    public void setTempoRespostaMs(Long tempoRespostaMs) {
        this.tempoRespostaMs = tempoRespostaMs;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    @Override
    public String toString() {
        return "HistoricoConsultaDto{" +
                "id=" + id +
                ", sintomasInformados='" + sintomasInformados + '\'' +
                ", tituloSugestao='" + tituloSugestao + '\'' +
                ", dataConsulta=" + dataConsulta +
                '}';
    }
}
