package com.project.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginUsuarioDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;

    public LoginUsuarioDto() {
    }

    public LoginUsuarioDto(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "LoginUsuarioDto{" +
                "nome='" + nome + '\'' +
                ", cpf='***" + (cpf != null && cpf.length() > 6 ? cpf.substring(cpf.length()-4) : "****") + '\'' +
                '}';
    }
}

