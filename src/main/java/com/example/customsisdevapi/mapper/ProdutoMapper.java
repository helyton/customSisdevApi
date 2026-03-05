package com.example.customsisdevapi.mapper;

import com.example.customsisdevapi.domain.Produto;
import com.example.customsisdevapi.dto.ProdutoRequestDTO;
import com.example.customsisdevapi.dto.ProdutoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO requestDTO) {
        Produto produto = new Produto();
        produto.setNome(requestDTO.nome());
        produto.setSku(requestDTO.sku());
        produto.setPreco(requestDTO.preco());
        return produto;
    }

    public void updateEntity(Produto produto, ProdutoRequestDTO requestDTO) {
        produto.setNome(requestDTO.nome());
        produto.setSku(requestDTO.sku());
        produto.setPreco(requestDTO.preco());
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getSku(),
                produto.getPreco(),
                produto.getAtivo(),
                produto.getCriadoEm()
        );
    }
}
