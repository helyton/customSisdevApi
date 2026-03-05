package com.example.customsisdevapi.repository;

import com.example.customsisdevapi.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsBySkuAndAtivoTrue(String sku);

    boolean existsBySkuAndAtivoTrueAndIdNot(String sku, Long id);

    Optional<Produto> findByIdAndAtivoTrue(Long id);

    List<Produto> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

    List<Produto> findByAtivoTrue();
}
