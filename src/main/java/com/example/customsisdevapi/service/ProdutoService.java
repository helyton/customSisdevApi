package com.example.customsisdevapi.service;

import com.example.customsisdevapi.domain.Produto;
import com.example.customsisdevapi.dto.ProdutoRequestDTO;
import com.example.customsisdevapi.dto.ProdutoResponseDTO;
import com.example.customsisdevapi.exception.BusinessException;
import com.example.customsisdevapi.exception.ResourceNotFoundException;
import com.example.customsisdevapi.integration.IntegracaoExternaClient;
import com.example.customsisdevapi.mapper.ProdutoMapper;
import com.example.customsisdevapi.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final IntegracaoExternaClient integracaoExternaClient;

    public ProdutoService(ProdutoRepository produtoRepository,
                          ProdutoMapper produtoMapper,
                          IntegracaoExternaClient integracaoExternaClient) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.integracaoExternaClient = integracaoExternaClient;
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO) {
        validarSkuNovo(requestDTO.sku());

        Produto produto = produtoMapper.toEntity(requestDTO);
        Produto salvo = produtoRepository.save(produto);
        ProdutoResponseDTO response = produtoMapper.toResponse(salvo);

        integracaoExternaClient.sincronizarProduto(response)
                .onErrorResume(error -> reactor.core.publisher.Mono.empty())
                .subscribe();

        return response;
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o id " + id));
        return produtoMapper.toResponse(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listar(String nome) {
        List<Produto> produtos;
        if (nome == null || nome.isBlank()) {
            produtos = produtoRepository.findByAtivoTrue();
        } else {
            produtos = produtoRepository.findByAtivoTrueAndNomeContainingIgnoreCase(nome);
        }
        return produtos.stream().map(produtoMapper::toResponse).toList();
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {
        Produto produto = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o id " + id));

        validarSkuAtualizacao(requestDTO.sku(), id);
        produtoMapper.updateEntity(produto, requestDTO);

        Produto salvo = produtoRepository.save(produto);
        return produtoMapper.toResponse(salvo);
    }

    @Transactional
    public void remover(Long id) {
        Produto produto = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o id " + id));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    private void validarSkuNovo(String sku) {
        if (produtoRepository.existsBySkuAndAtivoTrue(sku)) {
            throw new BusinessException("SKU já cadastrado para um produto ativo");
        }
    }

    private void validarSkuAtualizacao(String sku, Long id) {
        if (produtoRepository.existsBySkuAndAtivoTrueAndIdNot(sku, id)) {
            throw new BusinessException("SKU já cadastrado para outro produto ativo");
        }
    }
}
