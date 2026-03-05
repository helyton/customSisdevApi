package com.example.customsisdevapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "PRODUTO")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 120)
    private String nome;

    @Column(name = "SKU", nullable = false, unique = true, length = 60)
    private String sku;

    @Column(name = "PRECO", nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @Column(name = "ATIVO", nullable = false)
    private Boolean ativo;

    @Column(name = "CRIADO_EM", nullable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) {
            this.ativo = true;
        }
        if (this.criadoEm == null) {
            this.criadoEm = OffsetDateTime.now();
        }
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
