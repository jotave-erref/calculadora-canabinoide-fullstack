package br.com.jotave.cannabis_calculator.service;

import br.com.jotave.cannabis_calculator.client.OpenAIClient;
import br.com.jotave.cannabis_calculator.dto.openai.OpenAIRequestDTO;
import br.com.jotave.cannabis_calculator.dto.openai.OpenAIResponseDTO;
import br.com.jotave.cannabis_calculator.dto.response.ChatResponseDTO;
import br.com.jotave.cannabis_calculator.exception.OpenAIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pela interação com o agente de IA.
 * Gerencia o envio de perguntas, caching de respostas e tratamento de erros.
 */
@Service
@Slf4j
public class ChatService {

    private final OpenAIClient openAIClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${openai.max-tokens:500}")
    private int maxTokens;

    @Value("${openai.temperature:0.7}")
    private double temperature;

    public ChatService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    /**
     * Processa a pergunta do paciente e retorna a resposta do agente de IA.
     * As respostas são cacheadas por 24 horas para reduzir custos.
     *
     * @param pergunta A pergunta do paciente.
     * @return DTO com a resposta do agente.
     */
    @Cacheable(value = "chatResponses", key = "#pergunta.toLowerCase()")
    public ChatResponseDTO responder(String pergunta) {
        log.info("Processando pergunta: {}", pergunta);

        try {
            // Constrói a mensagem do sistema (system prompt)
            String systemPrompt = construirSystemPrompt();

            // Monta a requisição para a OpenAI
            OpenAIRequestDTO requestDTO = construirRequisicao(pergunta, systemPrompt);

            // Envia para a API OpenAI
            String authHeader = "Bearer " + apiKey;
            OpenAIResponseDTO resposta = openAIClient.chatCompletion(requestDTO, authHeader);

            // Extrai a resposta do modelo
            String conteudoResposta = extrairConteudo(resposta);
            log.info("Resposta gerada com sucesso. Tokens totais: {}", resposta.usage().totalTokens());

            return new ChatResponseDTO(conteudoResposta);

        } catch (OpenAIException e) {
            log.error("Erro ao chamar API OpenAI: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao processar pergunta: {}", e.getMessage(), e);
            throw new OpenAIException("Erro ao processar sua pergunta. Tente novamente.", e);
        }
    }

    /**
     * Constrói o system prompt que define o comportamento do agente.
     * Instrui o modelo a responder apenas sobre cannabis medicinal.
     */
    private String construirSystemPrompt() {
        return """
                Você é um assistente especializado em cannabis medicinal. 
                Respondas apenas perguntas sobre cannabis medicinal, seus usos terapêuticos, composição (CBD, THC), efeitos colaterais e interações medicamentosas.
                
                Diretrizes:
                - Forneça informações precisas e baseadas em evidências científicas.
                - Se você não souber a resposta, diga: "Não tenho certeza sobre isso. Consulte um médico especializado."
                - Sempre recomende consultar um profissional médico para decisões clínicas.
                - Mantenha um tom profissional, educado e empático.
                - Respostas devem ser concisas (máximo 3-4 parágrafos).
                """;
    }

    /**
     * Constrói a requisição para o endpoint Chat Completions da OpenAI.
     */
    private OpenAIRequestDTO construirRequisicao(String pergunta, String systemPrompt) {
        var messages = List.of(
                new OpenAIRequestDTO.MessageDTO("system", systemPrompt),
                new OpenAIRequestDTO.MessageDTO("user", pergunta)
        );

        return new OpenAIRequestDTO(
                model,
                messages,
                maxTokens,
                temperature
        );
    }

    /**
     * Extrai o conteúdo de texto da resposta da OpenAI.
     */
    private String extrairConteudo(OpenAIResponseDTO resposta) {
        if (resposta.choices() == null || resposta.choices().isEmpty()) {
            throw new OpenAIException("Resposta vazia da API OpenAI.");
        }

        var primeiraEscolha = resposta.choices().get(0);
        if (primeiraEscolha.message() == null) {
            throw new OpenAIException("Estrutura de resposta inválida da API OpenAI.");
        }

        return primeiraEscolha.message().content();
    }
}
