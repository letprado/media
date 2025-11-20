package com.project.media.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class SugestaoRequestDto {

    @NotEmpty(message = "Lista de sintomas não pode estar vazia")
    @Size(min = 1, max = 10, message = "Deve informar entre 1 e 10 sintomas")
    private List<String> sintomas;

    public SugestaoRequestDto() {
    }

    public SugestaoRequestDto(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    @Override
    public String toString() {
        return "SugestaoRequestDto{" +
                "sintomas=" + sintomas +
                '}';
    }
}

