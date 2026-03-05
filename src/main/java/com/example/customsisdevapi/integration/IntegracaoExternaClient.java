package com.example.customsisdevapi.integration;

import com.example.customsisdevapi.dto.ProdutoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class IntegracaoExternaClient {

    private static final Logger log = LoggerFactory.getLogger(IntegracaoExternaClient.class);

    private final WebClient webClient;

    public IntegracaoExternaClient(@Qualifier("integracaoExternaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> consultarHealth() {
        return webClient.get()
                .uri("/health")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(300))
                        .doBeforeRetry(signal -> log.warn("Retry health externo tentativa {}", signal.totalRetriesInARow() + 1)))
                .doOnError(error -> log.error("Falha ao consultar health externo", error));
    }

    public Mono<Void> sincronizarProduto(ProdutoResponseDTO produto) {
        return webClient.post()
                .uri("/produtos-sync")
                .bodyValue(produto)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(300))
                        .doBeforeRetry(signal -> log.warn("Retry sync produto tentativa {}", signal.totalRetriesInARow() + 1)))
                .doOnError(error -> log.error("Falha ao sincronizar produto id={}", produto.id(), error));
    }
}
