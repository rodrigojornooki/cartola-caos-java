# Cartola Caos Java 🏆⚡

> Uma versão "invertida" do Cartola FC onde os piores desempenhos são premiados!

## 🚀 Deployment Rápido (GRATUITO)

### Opção 1: Render.com (Recomendado)
1. Faça fork deste repositório
2. Crie conta em [render.com](https://render.com)
3. Conecte seu repositório GitHub
4. O arquivo `render.yaml` já está configurado!
5. Acesse sua aplicação em poucos minutos

### Opção 2: Railway.app
1. Crie conta em [railway.app](https://railway.app)
2. Conecte seu repositório GitHub
3. Adicione PostgreSQL plugin
4. Deploy automático com `railway.json`

### Opção 3: Docker Local
```bash
# Clone o repositório
git clone https://github.com/rodrigojornooki/cartola-caos-java.git
cd cartola-caos-java

# Execute com Docker Compose
docker-compose up
```

Acesse: http://localhost:8080

## 📋 Sobre o Projeto

O **Cartola Caos** é uma API REST que inverte a lógica do fantasy game tradicional:
- ❌ **Cartões vermelhos** = pontos positivos
- ⚠️ **Cartões amarelos** = pontos positivos  
- 🥅 **Gols contra** = pontos positivos
- ❌ **Penaltis perdidos** = pontos positivos
- 🚫 **Faltas cometidas** = pontos positivos

## 🛠 Tecnologias

- **Java 17** ☕
- **Spring Boot 3.1.4** 🚀
- **PostgreSQL** 🐘
- **Docker** 🐳
- **Swagger UI** 📚

## 📡 API Endpoints

- **Swagger UI**: `/swagger-ui.html` 📚
- **Health Check**: `/actuator/health` 💚
- **API Docs**: `/api-docs` 📖

## 🔧 Configuração de Desenvolvimento

### Pré-requisitos
- Java 17+
- PostgreSQL
- Maven

### Executar Localmente
```bash
./mvnw spring-boot:run
```

## 🌍 Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DATABASE_URL` | URL PostgreSQL | `jdbc:postgresql://localhost:5432/cartola_db` |
| `DB_USERNAME` | Usuário do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | `password` |
| `CARTOLA_TOKEN` | Token da API Cartola | Obrigatório |

## 📖 Documentação Completa

Consulte [`DEPLOYMENT.md`](./DEPLOYMENT.md) para instruções detalhadas de deployment.

## 🤝 Contribuindo

1. Fork o projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 🎯 Status do Projeto

- ✅ API REST funcional
- ✅ Integração com Cartola FC
- ✅ Sistema de pontuação negativa
- ✅ Deploy gratuito configurado
- 🚧 Interface web (em desenvolvimento)

---

Feito com ❤️ para os amantes do futebol brasileiro 🇧🇷