# Documentação da API - Swagger

## 📚 Sobre o Swagger

Este projeto utiliza o Swagger (OpenAPI 3.0) para documentação interativa da API REST.

## 🚀 Como Acessar

Após iniciar a aplicação, acesse:

### Swagger UI (Interface Interativa)
```
http://localhost:8080/swagger-ui.html
```
ou
```
http://localhost:8080/swagger-ui/index.html
```

### API Docs (JSON)
```
http://localhost:8080/v3/api-docs
```

## 📋 Endpoints Documentados

### 1. **Jogadores** (`/api/jogadores`)
- GET `/api/jogadores` - Listar todos os jogadores
- GET `/api/jogadores/{id}` - Buscar jogador por ID
- GET `/api/jogadores/time/{timeId}` - Listar jogadores por time
- GET `/api/jogadores/posicao/{posicao}` - Listar jogadores por posição
- GET `/api/jogadores/cartola` - Listar jogadores do Cartola FC oficial
- GET `/api/jogadores/jogos-rodada` - Listar jogos da rodada
- GET `/api/jogadores/pontuados` - Listar jogadores pontuados
- POST `/api/jogadores` - Criar novo jogador
- PUT `/api/jogadores/{id}` - Atualizar jogador
- DELETE `/api/jogadores/{id}` - Deletar jogador

### 2. **Times** (`/api/times`)
- GET `/api/times` - Listar todos os times
- GET `/api/times/{id}` - Buscar time por ID
- GET `/api/times/usuario/{nomeUsuario}` - Buscar time por nome de usuário
- POST `/api/times` - Criar novo time
- PUT `/api/times/{id}` - Atualizar time
- DELETE `/api/times/{id}` - Deletar time

### 3. **Rodadas** (`/api/rodadas`)
- GET `/api/rodadas` - Listar todas as rodadas
- GET `/api/rodadas/{id}` - Buscar rodada por ID
- GET `/api/rodadas/numero/{numero}` - Buscar rodada por número
- POST `/api/rodadas` - Criar nova rodada
- PUT `/api/rodadas/{id}` - Atualizar rodada
- DELETE `/api/rodadas/{id}` - Deletar rodada

### 4. **Cartola** (`/api/cartola`)
- GET `/api/cartola/pontuacoes` - Buscar pontuações atuais
- GET `/api/cartola/mercado/status` - Status do mercado
- GET `/api/cartola/jogadores/{id}/historico` - Histórico do jogador

## 🎯 Funcionalidades do Swagger UI

1. **Testar Endpoints**: Clique em "Try it out" para executar requisições diretamente
2. **Ver Schemas**: Visualize a estrutura dos objetos JSON
3. **Responses**: Veja exemplos de respostas de sucesso e erro
4. **Autenticação**: Teste com diferentes credenciais (se aplicável)

## 🛠️ Dependências Utilizadas

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

## 📝 Customização

A configuração do Swagger está localizada em:
```
src/main/java/com/cartola/backend/config/SwaggerConfig.java
```

## 🔗 Links Úteis

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.0)

