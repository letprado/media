package com.project.media.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sintomas")
public class Sintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Nome do sintoma é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToMany(mappedBy = "sintomas")
    private Set<Sugestao> sugestoes = new HashSet<>();

    public Sintoma() {
    }

    public Sintoma(String nome, String descricao, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.ativo = true;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public Set<Sugestao> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(Set<Sugestao> sugestoes) {
        this.sugestoes = sugestoes;
    }

    @Override
    public String toString() {
        return "Sintoma{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}

