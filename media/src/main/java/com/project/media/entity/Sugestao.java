package com.project.media.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sugestoes")
public class Sugestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    @Column(name = "descricao", nullable = false, length = 1000)
    private String descricao;

    @NotBlank(message = "Tipo de atendimento é obrigatório")
    @Size(max = 50, message = "Tipo de atendimento deve ter no máximo 50 caracteres")
    @Column(name = "tipo_atendimento", nullable = false, length = 50)
    private String tipoAtendimento;

    @NotNull(message = "Prioridade é obrigatória")
    @Column(name = "prioridade", nullable = false)
    private Integer prioridade;

    @Size(max = 500, message = "Recomendações devem ter no máximo 500 caracteres")
    @Column(name = "recomendacoes", length = 500)
    private String recomendacoes;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sugestao_sintoma",
        joinColumns = @JoinColumn(name = "sugestao_id"),
        inverseJoinColumns = @JoinColumn(name = "sintoma_id")
    )
    private Set<Sintoma> sintomas = new HashSet<>();

    @OneToMany(mappedBy = "sugestao", fetch = FetchType.LAZY)
    private Set<HistoricoConsulta> historicos = new HashSet<>();

    public Sugestao() {
    }

    public Sugestao(String titulo, String descricao, String tipoAtendimento, Integer prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoAtendimento = tipoAtendimento;
        this.prioridade = prioridade;
        this.ativo = true;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        if (ativo == null) {
            ativo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Set<Sintoma> getSintomas() {
        return sintomas;
    }

    public void setSintomas(Set<Sintoma> sintomas) {
        this.sintomas = sintomas;
    }

    public Set<HistoricoConsulta> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Set<HistoricoConsulta> historicos) {
        this.historicos = historicos;
    }

    @Override
    public String toString() {
        return "Sugestao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", tipoAtendimento='" + tipoAtendimento + '\'' +
                ", prioridade=" + prioridade +
                ", ativo=" + ativo +
                '}';
    }
}

