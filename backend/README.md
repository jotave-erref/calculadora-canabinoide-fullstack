# Backend - Cannabinoid-Calculator
Calculadora de dosagem de canabinboides em extratos de cannabis
Este diretório contém a API RESTful construída com Java e Spring Boot.

## Tecnologias
- Java 17
- Spring Boot 3
- Spring Data JPA / Hibernate
- Maven
- MySQL / H2
- Flyway para migrações de banco de dados

## Configuração
Para rodar este projeto, certifique-se de que as configurações de banco de dados estão corretas no arquivo `src/main/resources/application.properties`.

```properties
# Exemplo de configuração para MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/calculadora_db
spring.datasource.username=root
spring.datasource.password=sua_senha
```

## Como Executar Sozinho
Você pode rodar apenas o backend usando o Maven wrapper:

```bash
# A partir do diretório /backend
./mvnw spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

## Principais Endpoints
- `POST /api/v1/calculadora/produtos`: Cadastra um novo produto.
- `GET /api/v1/calculadora/produtos`: Lista todos os produtos.
- `POST /api/v1/calculadora/calcular`: Calcula a dosagem a partir de um produto, gotas e medicamentos.
