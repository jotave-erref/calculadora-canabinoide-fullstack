# 🚀 GUIA COMPLETO — Rodar a Aplicação (Ponta-a-Ponta)

**Objetivo:** Ter tudo rodando em 10 minutos.

---

## ✅ Pré-requisitos

- [x] Java 17+ instalado (`java -version`)
- [x] Maven 3.8+ instalado (`mvn -v`)
- [x] Node.js 16+ instalado (`node -v`)
- [x] npm 8+ instalado (`npm -v`)
- [x] MySQL 8.0+ rodando localmente
- [x] OpenAI API key obtida
- [x] Variável de ambiente `OPENAI_API_KEY` configurada

### Configurar OPENAI_API_KEY (Windows PowerShell)

```powershell
# Permanente (recomendado)
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "sk-seu-token-aqui", "User")

# Verificar
$env:OPENAI_API_KEY
# Resposta: sk-... (sua chave)

# Se configurar sem reiniciar/reabrir terminal:
$env:OPENAI_API_KEY = "sk-seu-token-aqui"
```

### Criar Database MySQL

```bash
# Via MySQL command line ou client:
CREATE DATABASE cannabis_calculator CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Verificar
SHOW DATABASES;
# Deve listar: cannabis_calculator
```

---

## 📋 Estrutura de Diretórios

Certifique-se que tem a estrutura assim:

```
calculadora-fullstack/
├── backend/
│   ├── pom.xml
│   ├── .env.example
│   ├── src/
│   │   ├── main/java/...
│   │   └── main/resources/
│   │       └── application.properties
│   └── mvnw
├── frontend/
│   ├── package.json
│   ├── src/
│   │   ├── App.js
│   │   ├── components/
│   │   ├── hooks/
│   │   └── styles/
│   └── node_modules/ (será criado)
└── README.md
```

---

## 🎬 Passo-a-Passo: Rodar Tudo

### **TERMINAL 1: Backend (Java/Spring Boot)**

```bash
# Navegar para backend
cd d:\ws-java\calculadora-fullstack\backend

# Limpar, compilar e rodar
.\mvnw clean compile spring-boot:run

# Esperado após ~10 segundos:
# 
# Tomcat started on port(s): 8080 (http)
#
# Isso significa: ✅ Backend está rodando!
```

**Mantenha este terminal aberto** (o backend precisa estar rodando sempre)

---

### **TERMINAL 2: Frontend (React)**

Abra um **novo terminal** (não feche o primeiro):

```bash
# Navegar para frontend
cd d:\ws-java\calculadora-fullstack\frontend

# Instalar dependências (primeira vez apenas)
npm install

# Rodar a aplicação
npm start

# Esperado após ~30 segundos:
#
# Local:     http://localhost:3000
# Browser abre automaticamente
#
# Isso significa: ✅ Frontend está rodando!
```

---

## 🧪 Testar a Aplicação

### 1. **Teste da Calculadora Básica**

1. No browser aberto (http://localhost:3000):
   - Selecione um produto no dropdown
   - Digite 5 em "Número de Gotas"
   - Clique em "Calcular Dosagem"
   - Deve aparecer resultados com concentrações de CBD/THC

✅ Se funcionar: Calculadora OK!

---

### 2. **Teste do Chat Assistant**

1. No mesmo browser:
   - Procure o botão **💬** no canto **inferior direito**
   - Clique nele (deve expandir uma caixa de chat)
   - Digite: **"O que é CBD?"**
   - Clique em **📤** (enviar)
   - Aguarde 2-3 segundos...
   - Deve aparecer resposta da IA

✅ Se funcionar: Chat OK!

---

### 3. **Teste via Postman (Validação Extra)**

Se quiser validar diretamente sem passar pelo React:

**Request:**
```
METHOD:  POST
URL:     http://localhost:8080/api/v1/chat/ask
BODY (raw JSON):
{
  "pergunta": "Quais são os efeitos colaterais da cannabis medicinal?"
}
```

**Response Esperada (200 OK):**
```json
{
  "resposta": "A cannabis medicinal pode ter efeitos colaterais como...",
  "fonte": "Agente de IA - Cannabis Medicinal"
}
```

✅ Se receber essa resposta: Integração OK!

---

## 🛑 Parar a Aplicação

### Desligar Backend
```bash
# No terminal 1 onde rodou o backend:
# Pressione: Ctrl + C
```

### Desligar Frontend
```bash
# No terminal 2 onde rodou o React:
# Pressione: Ctrl + C
# Confirme: Y (yes)
```

---

## 🐛 Troubleshooting

### **"Backend não inicializa"**

Erro: `Application run failed`

**Solução:**
```bash
# 1. Verifique MySQL
mysql -u root -p
# Deve conectar sem erro

# 2. Verifique OpenAI API key
echo $env:OPENAI_API_KEY
# Deve retornar: sk-...

# 3. Se falhar, reconfigure
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "sk-novo-token", "User")
# Feche e reabra o terminal!

# 4. Limpe cache Maven
.\mvnw clean
.\mvnw spring-boot:run
```

---

### **"Frontend não abre"**

Erro: `npm WARN`

**Solução:**
```bash
# 1. Delete node_modules
rm -r node_modules

# 2. Limpe cache npm
npm cache clean --force

# 3. Reinstale
npm install

# 4. Rode novamente
npm start
```

---

### **"Chat não responde"**

Erro: `503 Service Unavailable`

**Causas possíveis:**
- [ ] Backend não está rodando (verifique terminal 1)
- [ ] OpenAI API key inválida (reconfigure)
- [ ] Rate limit atingido (aguarde 60 segundos)
- [ ] Servidor OpenAI offline (tente mais tarde)

**Solução:**
```bash
# 1. Verifique status backend
curl http://localhost:8080/api/v1/calculadora/produtos
# Deve retornar JSON com produto

# 2. Teste POST direto
curl -X POST http://localhost:8080/api/v1/chat/ask \
  -H "Content-Type: application/json" \
  -d '{"pergunta": "teste"}'
# Deve retornar resposta IA ou erro específico
```

---

### **"Erro CORS"**

Erro no console: `Access to XMLHttpRequest blocked`

**Solução:**
Isso significa o backend não está rodando. Verifique:
```bash
# 1. Backend rodando na porta 8080?
netstat -ano | findstr 8080
# Deve listar um processo

# 2. Se não, reinicie:
cd backend
.\mvnw spring-boot:run
```

---

## 🎨 Customizações Rápidas

### Mudar cores do chat widget

Edite `frontend/src/styles/ChatWidget.css`:

```css
.chat-widget-button {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF5252 100%); /* Cores vermelhas */
}
```

### Mudar tamanho da caixa de chat

Edite `frontend/src/styles/ChatWidget.css`:

```css
.chat-widget-container {
  width: 450px;   /* Aumentou de 380px */
  height: 600px;  /* Aumentou de 500px */
}
```

### Mudar ícone do botão

Edite `frontend/src/components/ChatWidget.js`:

```javascript
<span className="chat-icon">🤖</span>  // Mude 💬 para 🤖 ou outro emoji
```

---

## 📊 Validação Rápida (Checklist)

Execute esto antes de considerar "pronto":

- [ ] Backend inicia: `Tomcat started on port(s): 8080`
- [ ] Frontend inicia: Browser abre em http://localhost:3000
- [ ] Calculadora funciona: Seleciona produto + calcula dosagem
- [ ] Chat responde: Clica 💬, digita pergunta, recebe resposta IA
- [ ] Sem erros console: F12 → Console → nenhum erro vermelho
- [ ] Postman 200 OK: POST /api/v1/chat/ask retorna resposta

✅ Se todos passarem: **SUCESSO COMPLETO!**

---

## 📞 Contato com Suporte IA

Se tiver dúvidas durante a execução:

1. **Backend não compila?** → Verifique `ANALISE_COMPATIBILIDADE.md`
2. **Frontend quebrado?** → Verifique `FASE_3_CHAT_WIDGET.md`
3. **Chat não funciona?** → Verifique `CHAT_AGENT_SETUP.md`
4. **Análise geral?** → Verifique `ANALISE_CODIGO_COMPLETA.md`

---

## 🎉 Parabéns!

Você conseguiu rodar com sucesso:
- ✅ Backend Java/Spring Boot 3 com integração OpenAI
- ✅ Frontend React 18 com chat widget
- ✅ Chat agent IA em produção (MVP)
- ✅ Calculadora de dosagem de cannabis

**Próximo passo:** Explorar o código, customizar conforme necessário, ou adicionar features (testes, dark mode, etc).

---

**Data:** 23 Abril 2026  
**Versão:** 1.0 (MVP)  
**Status:** 🟢 **FUNCIONANDO**

---

*Obs: Se deixar o projeto rodando por muito tempo, monitore o consumo de API OpenAI para evitar surpresas na fatura! (~$0.0002 por pergunta, cache em 24h)*
