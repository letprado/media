package com.project.media.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_consultas")
public class HistoricoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Sintomas informados são obrigatórios")
    @Size(min = 5, max = 1000, message = "Sintomas devem ter entre 5 e 1000 caracteres")
    @Column(name = "sintomas_informados", nullable = false, length = 1000)
    private String sintomasInformados;

    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 dígitos")
    @Column(name = "cpf_paciente", length = 11)
    private String cpfPaciente;

    @Column(name = "nome_paciente", length = 200)
    private String nomePaciente;

    @NotNull(message = "Sugestão é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sugestao_id", nullable = false)
    private Sugestao sugestao;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "data_consulta", nullable = false)
    private LocalDateTime dataConsulta;

    @Column(name = "tempo_resposta_ms")
    private Long tempoRespostaMs;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    public HistoricoConsulta() {
    }

    public HistoricoConsulta(String sintomasInformados, Sugestao sugestao) {
        this.sintomasInformados = sintomasInformados;
        this.sugestao = sugestao;
        this.dataConsulta = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (dataConsulta == null) {
            dataConsulta = LocalDateTime.now();
        }
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

    public Sugestao getSugestao() {
        return sugestao;
    }

    public void setSugestao(Sugestao sugestao) {
        this.sugestao = sugestao;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    @Override
    public String toString() {
        return "HistoricoConsulta{" +
                "id=" + id +
                ", sintomasInformados='" + sintomasInformados + '\'' +
                ", dataConsulta=" + dataConsulta +
                ", tempoRespostaMs=" + tempoRespostaMs +
                '}';
    }
}
