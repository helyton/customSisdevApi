package com.example.customsisdevapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "SKU é obrigatório")
        @Size(max = 60, message = "SKU deve ter no máximo 60 caracteres")
        String sku,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "Preço deve ser maior ou igual a zero")
        BigDecimal preco
) {
}
