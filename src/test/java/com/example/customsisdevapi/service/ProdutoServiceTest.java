package com.example.customsisdevapi.service;

import com.example.customsisdevapi.domain.Produto;
import com.example.customsisdevapi.dto.ProdutoRequestDTO;
import com.example.customsisdevapi.dto.ProdutoResponseDTO;
import com.example.customsisdevapi.exception.BusinessException;
import com.example.customsisdevapi.integration.IntegracaoExternaClient;
import com.example.customsisdevapi.mapper.ProdutoMapper;
import com.example.customsisdevapi.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private IntegracaoExternaClient integracaoExternaClient;

    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        produtoService = new ProdutoService(produtoRepository, new ProdutoMapper(), integracaoExternaClient);
    }

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Notebook Gamer", "SKU123", new BigDecimal("5000.00"));

        Produto salvo = new Produto();
        salvo.setId(1L);
        salvo.setNome(request.nome());
        salvo.setSku(request.sku());
        salvo.setPreco(request.preco());
        salvo.setAtivo(true);
        salvo.setCriadoEm(OffsetDateTime.now());

        when(produtoRepository.existsBySkuAndAtivoTrue("SKU123")).thenReturn(false);
        when(produtoRepository.save(any(Produto.class))).thenReturn(salvo);
        when(integracaoExternaClient.sincronizarProduto(any(ProdutoResponseDTO.class))).thenReturn(Mono.empty());

        ProdutoResponseDTO response = produtoService.criar(request);

        assertEquals(1L, response.id());
        assertEquals("Notebook Gamer", response.nome());
        assertEquals("SKU123", response.sku());
    }

    @Test
    void deveLancarErroQuandoSkuDuplicado() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Produto Duplicado", "SKU_DUP", new BigDecimal("10.00"));
        when(produtoRepository.existsBySkuAndAtivoTrue("SKU_DUP")).thenReturn(true);

        assertThrows(BusinessException.class, () -> produtoService.criar(request));
    }
}
