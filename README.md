# customSisdevApi

API REST em Java 21 (Spring Boot 3) com Oracle, CRUD de `Produto`, integração HTTP externa e foco em produção.

## Decisões de arquitetura
- **Spring Boot 3.3 + Java 21** para base moderna e estável.
- **Spring Data JPA + Hibernate** para persistência.
- **Flyway** para versionamento de banco e provisionamento reprodutível.
- **WebClient** para integrações externas por suportar facilmente timeout, retry e observabilidade reativa.
- **Tratamento global de erros** padronizando resposta JSON (`status`, `message`, `timestamp`, `path`).
- **Soft delete**: exclusão lógica (`ativo=false`) para preservar histórico.

## Estrutura de pastas

```text
src
├── main
│   ├── java/com/example/customsisdevapi
│   │   ├── config
│   │   ├── controller
│   │   ├── domain
│   │   ├── dto
│   │   ├── exception
│   │   ├── integration
│   │   ├── mapper
│   │   ├── repository
│   │   └── service
│   └── resources
│       ├── db/migration
│       ├── application.yml
│       └── logback-spring.xml
└── test
    └── java/com/example/customsisdevapi
        ├── controller
        └── service
```

## Pré-requisitos
- Java 21
- Maven 3.9+
- Oracle Database (ex.: XE)

## Configuração Oracle
Ajuste no `src/main/resources/application.yml`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Exemplo atual:
- URL: `jdbc:oracle:thin:@//localhost:1521/XEPDB1`
- Driver: `oracle.jdbc.OracleDriver`
- Dialeto: `org.hibernate.dialect.OracleDialect`
- `ddl-auto: validate`

## Rodando localmente

```bash
mvn clean test
mvn spring-boot:run
```

Swagger/OpenAPI:
- `http://localhost:8080/swagger-ui.html`

## Endpoints do MVP
- `POST /api/produtos`
- `GET /api/produtos/{id}`
- `GET /api/produtos?nome=...`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}` (soft delete)

## Exemplos curl

### Criar produto
```bash
curl -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Monitor 4K",
    "sku": "MON-4K-001",
    "preco": 2899.90
  }'
```

### Buscar por id
```bash
curl -X GET "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json"
```

### Listar com filtro
```bash
curl -X GET "http://localhost:8080/api/produtos?nome=monitor" \
  -H "Content-Type: application/json"
```

### Atualizar
```bash
curl -X PUT "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Monitor 4K HDR",
    "sku": "MON-4K-001",
    "preco": 2999.90
  }'
```

### Soft delete
```bash
curl -X DELETE "http://localhost:8080/api/produtos/1" \
  -H "Content-Type: application/json"
```

## Integração externa
Componente: `IntegracaoExternaClient`
- `GET /health`
- `POST /produtos-sync`

Configurações:
- `integracao.externa.base-url`
- `integracao.externa.connect-timeout`
- `integracao.externa.read-timeout`

Inclui retry simples (2 tentativas) e logs de falha.

## Pontos de extensão
- Adicionar paginação nos endpoints de listagem.
- Incluir autenticação/autorização (Spring Security + JWT/OAuth2).
- Observabilidade avançada (Micrometer/Prometheus/Tracing).
- Mensageria para sincronização assíncrona robusta (Kafka/Rabbit).
