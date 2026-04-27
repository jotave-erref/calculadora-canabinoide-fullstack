package br.com.jotave.cannabis_calculator.client;

import br.com.jotave.cannabis_calculator.dto.openai.OpenAIRequestDTO;
import br.com.jotave.cannabis_calculator.dto.openai.OpenAIResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Cliente OpenFeign para integração com a API OpenAI.
 * Encapsula as chamadas ao endpoint de Chat Completions.
 * O tratamento de erros é feito globalmente em FeignGlobalConfig.
 */
@FeignClient(
        name = "openai-client",
        url = "${openai.api.url}"
)
public interface OpenAIClient {

    /**
     * Chama o endpoint de Chat Completions do OpenAI.
     *
     * @param request DTO com a pergunta e configurações do modelo.
     * @param authHeader Header de autenticação com a chave API.
     * @return DTO com a resposta do modelo.
     */
    @PostMapping("/v1/chat/completions")
    OpenAIResponseDTO chatCompletion(
            @RequestBody OpenAIRequestDTO request,
            @RequestHeader("Authorization") String authHeader
    );
}
