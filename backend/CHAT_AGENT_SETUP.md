# 🤖 Guia de Integração com OpenAI — Chat Agent

## 📋 Resumo

Você agora tem um **agente de IA integrado** que responde perguntas sobre cannabis medicinal. A integração usa:

- **OpenFeign** para comunicação comOpenAI API
- **Spring Cache** para reutilizar respostas (reduz custos)
- **Bean Validation** para validar entrada
- **Error handling** robusto com exceções customizadas

---

## ⚙️ Configuração Inicial

### 1. Obtenha a Chave API OpenAI

1. Acesse: https://platform.openai.com/account/api-keys
2. Crie uma nova chave (secret key)
3. **Copie e guarde em um local seguro** (você não verá novamente)

### 2. Configure a Variável de Ambiente

**Opção A: Via variável de sistema (recomendado)**

**Windows (PowerShell):**
```powershell
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "sk-...", "User")
# Feche e reabra o VS Code/IDE para aplicar
```

**Windows (CMD):**
```cmd
setx OPENAI_API_KEY sk-...
# Feche e reabra o VS Code/IDE para aplicar
```

**macOS/Linux (bash):**
```bash
export OPENAI_API_KEY="sk-..."
# Ou adicione permanentemente ao ~/.bashrc ou ~/.zshrc
```

**Opção B: Via arquivo `.env` (para desenvolvimento local)**

1. Copie o arquivo `backend/.env.example` para `backend/.env`
2. Edite e preencha a chave:
   ```properties
   OPENAI_API_KEY=sk-seu-token-aqui
   ```
3. ⚠️ **NUNCA committe `.env`** — está listado em `.gitignore`

### 3. Configure Maven para ler variáveis de ambiente

Se usar `.env` local, instale a extensão **VS Code Dotenv**:
```
Ext: Dotenv
```

Ou defina a variável globalmente antes de rodar o Maven:
```powershell
$env:OPENAI_API_KEY = "sk-..."
cd backend
./mvnw clean package
```

---

## 🚀 Como Usar

### Build e Run

```bash
cd backend

# Build
./mvnw clean compile

# Rodar
./mvnw spring-boot:run

# Testes unitários
./mvnw test
```

### Testar o Endpoint

**Via cURL (Windows PowerShell):**
```powershell
$body = @{
    pergunta = "O que é CBD e qual é a diferença entre CBD e THC?"
} | ConvertTo-Json

Invoke-WebRequest `
  -Uri "http://localhost:8080/api/v1/chat/ask" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body
```

**Via cURL (Linux/macOS):**
```bash
curl -X POST http://localhost:8080/api/v1/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"pergunta": "O que é CBD e qual é a diferença entre CBD e THC?"}'
```

**Via Postman:**
1. New → Request
2. Method: `POST`
3. URL: `http://localhost:8080/api/v1/chat/ask`
4. Body → raw → JSON:
   ```json
   {
     "pergunta": "O que é CBD e qual é a diferença entre CBD e THC?"
   }
   ```
5. Send

---

## 💰 Otimizações de Custo

| Estratégia | Impacto |
|-----------|---------|
| **GPT-3.5-turbo** (padrão) | 75% mais barato que GPT-4 |
| **max_tokens=500** | Limita resposta; economiza tokens |
| **Cache de respostas** | 1ªquery = $0.0002; 2ª em diante = $0 |
| **Rate limit (10s)** | Evita requests em loop |
| **Validação de input** | Rejeita queries vazias/spam |

**Custo estimado:**
- Pergunta nova: ~$0.0002 (500 tokens)
- Pergunta em cache (24h): $0 (reutiliza cache)
- 100 perguntas novas/dia: ~$0.02

---

## 🔍 Fluxo de Requisição

```
Cliente (React/Postman)
    ↓ POST /api/v1/chat/ask
ChatController
    ↓
ChatService.responder()
    ↓ Verifica Cache?
    ├─ SIM → Retorna resposta cacheada (0 custo)
    └─ NÃO → Constrói system prompt + requisição
        ↓ Via OpenFeign
    OpenAIClient.chatCompletion()
        ↓ HTTP POST
    OpenAI API (gpt-3.5-turbo)
        ↓
    OpenAIResponseDTO
        ↓
    Armazena em cache (24h)
        ↓
ChatResponseDTO
    ← Response 200 OK
Cliente
```

---

## 📋 Estrutura de Arquivos Criada

```
backend/
├── src/main/java/br/com/jotave/cannabis_calculator/
│   ├── client/
│   │   ├── OpenAIClient.java           (Interface Feign)
│   │   └── OpenAIFeignConfig.java      (Config Feign + Error handling)
│   ├── controller/
│   │   └── ChatController.java         (Endpoint /api/v1/chat/ask)
│   ├── service/
│   │   └── ChatService.java            (Lógica IA + cache)
│   ├── dto/
│   │   ├── request/
│   │   │   └── ChatRequestDTO.java
│   │   ├── response/
│   │   │   └── ChatResponseDTO.java
│   │   └── openai/
│   │       ├── OpenAIRequestDTO.java
│   │       └── OpenAIResponseDTO.java
│   └── exception/
│       └── OpenAIException.java
├── src/main/resources/
│   └── application.properties           (Configuração OpenAI + Feign)
├── .env.example                         (Template de variáveis)
└── pom.xml                              (Dependências: OpenFeign, Cache)
```

---

## ⚠️ Troubleshooting

| Problema | Solução |
|----------|---------|
| **`401 Unauthorized`** | Chave API inválida/expirada. Verifique em https://platform.openai.com |
| **`429 Too Many Requests`** | Rate limit atingido. Aguarde 60s antes de nova requisição |
| **`503 Service Unavailable`** | Servidor OpenAI offline. Tente novamente em alguns minutos |
| **`Connection timeout`** | Firewall bloqueando. Verifique conexão com `api.openai.com` |
| **`@EnableFeignClients not found`** | Maven não importou OpenFeign. Rode `./mvnw clean compile` |

---

## ✅ Checklist de Setup

- [ ] Chave API OpenAI obtida de https://platform.openai.com
- [ ] Variável `OPENAI_API_KEY` definida no sistema ou `.env`
- [ ] `./mvnw clean compile` rodou sem erros
- [ ] Backend iniciou em `http://localhost:8080`
- [ ] Teste POST `/api/v1/chat/ask` retornou resposta válida
- [ ] Response contém campo `resposta` e `fonte`

---

## 🎯 Próximos Passos

1. **Frontend React**: Criar componente ChatBox (próximo guia)
2. **Testes**: Adicionar testes unitários + mock do OpenAIClient
3. **Auditoria**: Opcional — guardar conversas no BD para análise
4. **Rate limiting**: Implementar limite por IP/usuário (sem autenticação)

---

**Dúvidas?** Teste o endpoint com cURL primeiro, depois integre no frontend React.
