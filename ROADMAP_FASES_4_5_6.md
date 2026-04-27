# 🚀 ROADMAP FUTURO — Fases 4, 5 e 6

Após confirmar que tudo está funcionando (MVP em Phase 3), aqui estão os próximos passos recomendados para levar a aplicação a **produção profissional**.

---

## 📊 Visão Geral

```
Phase 1: ✅ Backend básico (Spring Boot + MySQL)
Phase 2: ✅ Integração OpenAI (Feign + Cache)
Phase 3: ✅ Frontend React + Chat Widget (MVP)
        ↓
Phase 4: 🔧 TESTES & QUALIDADE (2-3 dias)
Phase 5: 🔐 SEGURANÇA & PERFORMANCE (3-4 dias)
Phase 6: 📱 DEPLOYMENT & MONITORAMENTO (2-3 dias)
        ↓
Phase 7: 🎯 FEATURES AVANÇADAS (opcional)
```

---

## **PHASE 4: TESTES & QUALIDADE** 🧪

### Objetivo
Garantir que todo o código funciona corretamente através de testes automatizados.

### 4.1 Unit Tests (Backend)

**Arquivo:** `backend/src/test/java/br/com/jotave/cannabis_calculator/service/ChatServiceTest.java`

```java
package br.com.jotave.cannabis_calculator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.jotave.cannabis_calculator.client.OpenAIClient;
import br.com.jotave.cannabis_calculator.dto.response.OpenAIResponseDTO;
import br.com.jotave.cannabis_calculator.exception.OpenAIException;

@DisplayName("ChatService Testes Unitários")
class ChatServiceTest {

    @Mock
    private OpenAIClient openAIClient;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar resposta válida para pergunta simples")
    void testResponderComSucesso() {
        // Given (Dado que)
        String pergunta = "O que é CBD?";
        OpenAIResponseDTO mockResponse = new OpenAIResponseDTO(
            List.of(new OpenAIResponseDTO.ChoiceDTO("Resposta mock", 0)),
            new OpenAIResponseDTO.UsageDTO(5, 20, 25)
        );
        when(openAIClient.chat(any())).thenReturn(mockResponse);

        // When (Quando)
        String resultado = chatService.responder(pergunta);

        // Then (Então)
        assertNotNull(resultado);
        assertTrue(resultado.contains("Resposta mock"));
        verify(openAIClient, times(1)).chat(any());
    }

    @Test
    @DisplayName("Deve cachear segunda chamada idêntica")
    void testCacheMemFuncionaDois() {
        // Given
        String pergunta = "O que é CBD?";
        // ... mock setup ...

        // When (chamar 2x com mesma pergunta)
        chatService.responder(pergunta);
        chatService.responder(pergunta);

        // Then (OpenAI deve ser chamado apenas 1x, segunda vem do cache)
        verify(openAIClient, times(1)).chat(any());
    }

    @Test
    @DisplayName("Deve lançar exceção para credenciais inválidas")
    void testOpenAIException401() {
        // Given
        String pergunta = "teste";
        when(openAIClient.chat(any())).thenThrow(
            new OpenAIException("Credenciais inválidas", 401)
        );

        // When & Then
        assertThrows(OpenAIException.class, () -> {
            chatService.responder(pergunta);
        });
    }
}
```

**Como rodar:**
```bash
cd backend
.\mvnw test -Dtest=ChatServiceTest
# Esperado: BUILD SUCCESS com 3 tests passed
```

---

### 4.2 Integration Tests (Backend)

**Arquivo:** `backend/src/test/java/br/com/jotave/cannabis_calculator/controller/ChatControllerIntegrationTest.java`

```java
package br.com.jotave.cannabis_calculator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPostChatAskComSucesso() throws Exception {
        mockMvc.perform(post("/api/v1/chat/ask")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"pergunta\": \"O que é CBD?\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resposta").exists())
        .andExpect(jsonPath("$.fonte").exists());
    }

    @Test
    void testPostChatAskComPerguntaVazia() throws Exception {
        mockMvc.perform(post("/api/v1/chat/ask")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"pergunta\": \"\"}")
        )
        .andExpect(status().isBadRequest()); // Validação deve rejeitar
    }
}
```

**Como rodar:**
```bash
cd backend
.\mvnw test -Dtest=ChatControllerIntegrationTest
```

---

### 4.3 Frontend Tests (React)

**Arquivo:** `frontend/src/components/ChatWidget.test.js`

```javascript
import { render, screen, fireEvent } from '@testing-library/react';
import ChatWidget from './ChatWidget';

describe('ChatWidget Component', () => {
  
  it('deve renderizar botão de chat', () => {
    render(<ChatWidget />);
    const button = screen.getByRole('button');
    expect(button).toBeInTheDocument();
  });

  it('deve expandir chat ao clicar no botão', () => {
    render(<ChatWidget />);
    const button = screen.getByRole('button');
    
    fireEvent.click(button);
    
    const chatBox = screen.getByTestId('chat-box');
    expect(chatBox).toHaveClass('expanded');
  });

  it('deve fechar chat ao clicar novamente', () => {
    render(<ChatWidget />);
    const button = screen.getByRole('button');
    
    fireEvent.click(button);
    fireEvent.click(button);
    
    const chatBox = screen.getByTestId('chat-box');
    expect(chatBox).not.toHaveClass('expanded');
  });
});
```

**Como rodar:**
```bash
cd frontend
npm test -- ChatWidget.test.js
```

---

### 4.4 Code Coverage

```bash
# Backend: Maven
cd backend
.\mvnw test jacoco:report
# Rel: target/site/jacoco/index.html (abrir no browser)

# Frontend: Jest
cd frontend
npm test -- --coverage
# Rel: coverage/lcov-report/index.html
```

**Meta recomendada:** 80%+ coverage para código crítico.

---

## **PHASE 5: SEGURANÇA & PERFORMANCE** 🔐

### 5.1 Spring Security + JWT

**Arquivo:** `backend/src/main/java/br/com/jotave/cannabis_calculator/config/SecurityConfig.java`

```java
package br.com.jotave.cannabis_calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/chat/ask").permitAll() // Público temporário
                .antMatchers("/api/v1/calculadora/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        return http.build();
    }
}
```

**Arquivo:** `backend/src/main/java/br/com/jotave/cannabis_calculator/controller/AuthController.java`

```java
package br.com.jotave.cannabis_calculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        // TODO: Validar credenciais, gerar JWT
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        // TODO: Criar novo usuário com senha criptografada
        return ResponseEntity.status(201).build();
    }
}
```

**Esforço:** 2-3 dias | **Complexidade:** ⭐⭐⭐

---

### 5.2 Rate Limiting

```xml
<!-- pom.xml: Adicionar -->
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.10.0</version>
</dependency>
```

**Arquivo:** `backend/src/main/java/br/com/jotave/cannabis_calculator/filter/RateLimitFilter.java`

```java
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response,
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> 
            Bucket.builder()
                .addLimit(Limit.of(100, Refill.intervally(100, Duration.ofMinutes(1))))
                .build()
        );
        
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Rate limit exceeded");
        }
    }
}
```

**Esforço:** 1 dia | **Complexidade:** ⭐⭐

---

### 5.3 HTTPS + SSL

```bash
# Gerar certificado self-signed (dev)
keytool -genkey -alias tomcat -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 365

# application.properties
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=password
server.ssl.key-store-type=JKS
server.port=8443
```

**Esforço:** 1 dia | **Complexidade:** ⭐

---

### 5.4 Database Optimization

```sql
-- Adicionar índices para melhorar performance
CREATE INDEX idx_produto_nome ON produto(nome);
CREATE INDEX idx_composicao_produto_id ON composicao_canabinoide(produto_id);

-- Análise de queries lenta
EXPLAIN SELECT * FROM produto WHERE nome = 'Cannabis Sativa';
```

**Esforço:** 4 horas | **Complexidade:** ⭐

---

## **PHASE 6: DEPLOYMENT & MONITORAMENTO** 📱

### 6.1 Docker

**Arquivo:** `backend/Dockerfile`

```dockerfile
FROM openjdk:17-slim

COPY target/cannabis-calculator.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

**Arquivo:** `docker-compose.yml` (raiz do projeto)

```yaml
version: '3.8'

services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: cannabis_calculator
      MYSQL_ROOT_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      OPENAI_API_KEY: ${OPENAI_API_KEY}
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/cannabis_calculator
    depends_on:
      - db

  frontend:
    build: ./frontend
    ports:
      - "3000:3000"

volumes:
  mysql_data:
```

**Como usar:**
```bash
docker-compose up -d
# Aplicação sobe em http://localhost:3000
```

**Esforço:** 1 dia | **Complexidade:** ⭐⭐

---

### 6.2 Deploy em AWS

**Opções:**

| Serviço | Custo/mês | Esforço | Notes |
|---------|-----------|--------|-------|
| **EC2** | $5-20 | ⭐⭐⭐ | VPS completo, controle total |
| **Elastic Beanstalk** | $5-15 | ⭐⭐ | PaaS Spring Boot nativo |
| **Lambda + API Gateway** | $1-10 | ⭐⭐⭐⭐ | Serverless (complexo refactor) |
| **ECS + Fargate** | $10-30 | ⭐⭐⭐ | Containerizado, escalável |

**Recomendado:** Elastic Beanstalk (simplest) ou EC2 (mais controle).

---

### 6.3 Logging & Monitoring

**Arquivo:** `backend/src/main/resources/logback-spring.xml`

```xml
<configuration>
    <springProfile name="prod">
        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>/var/log/cannabis-calculator.log</file>
            <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <fileNamePattern>/var/log/cannabis-calculator.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
                <maxFileSize>10MB</maxFileSize>
                <maxHistory>30</maxHistory>
            </rollingPolicy>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="FILE"/>
        </root>
    </springProfile>
</configuration>
```

**Integração com Sentry (Error Tracking):**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.sentry</groupId>
    <artifactId>sentry-spring-boot-starter</artifactId>
    <version>6.25.0</version>
</dependency>
```

```properties
# application.properties
sentry.dsn=https://seu-sentry-dsn@sentry.io/123456
sentry.environment=production
sentry.traces-sample-rate=1.0
```

**Esforço:** 2 dias | **Complexidade:** ⭐⭐

---

### 6.4 CI/CD Pipeline

**Arquivo:** `.github/workflows/deploy.yml` (GitHub Actions)

```yaml
name: Deploy

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up Java
      uses: actions/setup-java@v2
      with:
        java-version: '17'
    
    - name: Build Backend
      run: |
        cd backend
        ./mvnw clean package -DskipTests
    
    - name: Build Frontend
      run: |
        cd frontend
        npm install
        npm run build
    
    - name: Deploy to Elastic Beanstalk
      uses: einaregilsson/beanstalk-deploy@v20
      with:
        aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        application_name: cannabis-calculator
        environment_name: cannabis-calculator-prod
        version_label: ${{ github.sha }}
        region: us-east-1
```

**Esforço:** 3 dias | **Complexidade:** ⭐⭐⭐

---

## **PHASE 7: FEATURES AVANÇADAS** 🎯 (Opcional)

### 7.1 Analytics & Dashboards

- [ ] Rastrear perguntas mais frequentes
- [ ] Dashboard de métricas (volume, latência, custo)
- [ ] Integração Google Analytics

### 7.2 API Public (Monetização)

- [ ] Criar plano freemium
- [ ] Stripe integration para pagamentos
- [ ] Rate limits por tier de usuário

### 7.3 Mobile App

- [ ] Flutter ou React Native refactor
- [ ] Push notifications
- [ ] Offline mode

### 7.4 Multi-Language

- [ ] i18n para português/inglês/espanhol
- [ ] Regional API endpoints

---

## 📋 Priorização Recomendada

```
SEMANA 1 (Priority HIGH):
├─ Phase 4.1: Unit tests ChatService
├─ Phase 4.2: Integration tests Controller
└─ Phase 4.3: Frontend tests

SEMANA 2 (Priority HIGH):
├─ Phase 5.1: Spring Security
├─ Phase 5.2: Rate limiting
└─ Phase 5.4: Database optimization

SEMANA 3 (Priority MEDIUM):
├─ Phase 6.1: Docker + docker-compose
├─ Phase 6.2: AWS Elastic Beanstalk deploy
└─ Phase 6.3: Logging/Sentry setup

SEMANA 4+ (Priority LOW):
├─ Phase 6.4: CI/CD GitHub Actions
├─ Phase 7.1: Analytics
└─ Phase 7.2-7.4: Features avançadas
```

---

## 🎓 Recursos de Aprendizado

| Tópico | Recurso |
|--------|---------|
| Spring Security | https://spring.io/guides/gs/securing-web/ |
| JWT Auth | https://jwt.io/introduction |
| Docker | https://docs.docker.com/get-started/ |
| AWS Elastic Beanstalk | https://docs.aws.amazon.com/elasticbeanstalk/ |
| GitHub Actions | https://docs.github.com/en/actions |
| JUnit 5 | https://junit.org/junit5/docs/current/user-guide/ |
| React Testing | https://testing-library.com/react |

---

## 💰 Estimativa de Custos (Mensal)

| Componente | Custo |
|-----------|-------|
| AWS EC2 (t3.small) | ~$10 |
| RDS MySQL | ~$15 |
| OpenAI API (10k req/mês) | ~$2 |
| CloudFront CDN | ~$5 |
| **TOTAL** | **~$32/mês** |

*Pode variar bastante conforme volume.*

---

## ✅ Checklist Final

Antes de considerar "pronto para produção":

- [ ] Coverage > 80%
- [ ] Sem warnings do Maven/npm
- [ ] HTTPS configurado
- [ ] Rate limiting ativo
- [ ] Logs centralizados
- [ ] Backups automáticos
- [ ] CI/CD pipeline passando
- [ ] Monitoria Sentry ativa
- [ ] Documentação API (Swagger)
- [ ] SLA definido (99.9%)

---

**Status:** 🟡 **EM DESENVOLVIMENTO**  
**Próxima Milestone:** Phase 4 (Testes)  
**ETA Produção:** 4-6 semanas  

---

*Revisado: 23 Abril 2026*  
*Versão: Roadmap v1.0*
