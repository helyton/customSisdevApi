package com.example.customsisdevapi.controller;

import com.example.customsisdevapi.dto.ProdutoRequestDTO;
import com.example.customsisdevapi.dto.ProdutoResponseDTO;
import com.example.customsisdevapi.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void deveCriarProdutoRetornando201() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Teclado", "SKU-TEC", new BigDecimal("199.90"));
        ProdutoResponseDTO response = new ProdutoResponseDTO(
                10L,
                "Teclado",
                "SKU-TEC",
                new BigDecimal("199.90"),
                true,
                OffsetDateTime.now()
        );

        when(produtoService.criar(any(ProdutoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.nome").value("Teclado"));
    }

    @Test
    void deveListarProdutos() throws Exception {
        when(produtoService.listar("tec")).thenReturn(List.of(
                new ProdutoResponseDTO(1L, "Teclado", "SKU-1", new BigDecimal("100.00"), true, OffsetDateTime.now())
        ));

        mockMvc.perform(get("/api/produtos").param("nome", "tec"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Teclado"));
    }
}
