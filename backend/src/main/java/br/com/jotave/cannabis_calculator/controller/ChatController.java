package br.com.jotave.cannabis_calculator.controller;

import br.com.jotave.cannabis_calculator.dto.request.ChatRequestDTO;
import br.com.jotave.cannabis_calculator.dto.response.ChatResponseDTO;
import br.com.jotave.cannabis_calculator.exception.OpenAIException;
import br.com.jotave.cannabis_calculator.service.ChatService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para endpoints relacionados ao agente de IA.
 * Gerencia as interações do paciente com o assistente de cannabis medicinal.
 */
@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Endpoint para fazer uma pergunta ao agente de IA.
     * HTTP Method: POST
     * URL: /api/v1/chat/ask
     *
     * @param requestDTO DTO contendo a pergunta do paciente.
     * @return Resposta do agente de IA sobre cannabis medicinal.
     */
    @PostMapping("/ask")
    public ResponseEntity<ChatResponseDTO> fazerPergunta(@Valid @RequestBody ChatRequestDTO requestDTO) {
        log.info("Recebida pergunta: {}", requestDTO.pergunta());

        try {
            ChatResponseDTO resposta = chatService.responder(requestDTO.pergunta());
            log.info("Pergunta processada com sucesso.");
            return ResponseEntity.ok(resposta);

        } catch (OpenAIException e) {
            log.error("Erro OpenAI: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ChatResponseDTO("Desculpe, o serviço de IA está temporariamente indisponível. Tente novamente em alguns minutos."));

        } catch (Exception e) {
            log.error("Erro inesperado ao processar pergunta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponseDTO("Erro ao processar sua pergunta. Tente novamente."));
        }
    }
}
