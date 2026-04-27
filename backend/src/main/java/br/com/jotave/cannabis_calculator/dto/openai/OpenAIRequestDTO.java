package br.com.jotave.cannabis_calculator.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para requisição à API OpenAI.
 * Segue o padrão da API de Chat Completions do OpenAI.
 */
public record OpenAIRequestDTO(

        String model,

        List<MessageDTO> messages,

        @JsonProperty("max_tokens")
        int maxTokens,

        double temperature
) {

    /**
     * DTO para representar uma mensagem na conversa.
     */
    public record MessageDTO(
            String role,
            String content
    ) {}
}
