package br.com.jotave.cannabis_calculator.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para resposta da API OpenAI.
 * Mapeia a estrutura de resposta do endpoint Chat Completions.
 */
public record OpenAIResponseDTO(

        String id,

        Object object,

        long created,

        String model,

        List<ChoiceDTO> choices,

        UsageDTO usage
) {

    /**
     * DTO para representar uma opção de resposta.
     */
    public record ChoiceDTO(

            int index,

            MessageDTO message,

            @JsonProperty("finish_reason")
            String finishReason
    ) {

        public record MessageDTO(
                String role,
                String content
        ) {}
    }

    /**
     * DTO para rastrear o uso de tokens.
     */
    public record UsageDTO(

            @JsonProperty("prompt_tokens")
            int promptTokens,

            @JsonProperty("completion_tokens")
            int completionTokens,

            @JsonProperty("total_tokens")
            int totalTokens
    ) {}
}
