package com.example.abastecimento_api.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Placa é obrigatória")
    @Column(name = "placa_veiculo")
    private String placa;

    @Positive(message = "Quilometragem deve ser maior que zero")
    private Integer quilometragem;

    @Past(message = "Data e hora não podem ser no futuro")
    private LocalDateTime dataHora;

    @Positive(message = "Valor total deve ser maior que zero")
    private double valorTotal;

    public Abastecimento() {
    }

    public Abastecimento(Long id, @NotBlank(message = "Placa é obrigatória") String placa,
            @Positive(message = "Quilometragem deve ser maior que zero") Integer quilometragem,
            @Past(message = "Data e hora não podem ser no futuro") LocalDateTime dataHora,
            @Positive(message = "Valor total deve ser maior que zero") double valorTotal) {
        this.id = id;
        this.placa = placa;
        this.quilometragem = quilometragem;
        this.dataHora = dataHora;
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Integer quilometragem) {
        this.quilometragem = quilometragem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}