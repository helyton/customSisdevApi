package com.example.customsisdevapi.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String sku,
        BigDecimal preco,
        Boolean ativo,
        OffsetDateTime criadoEm
) {
}
