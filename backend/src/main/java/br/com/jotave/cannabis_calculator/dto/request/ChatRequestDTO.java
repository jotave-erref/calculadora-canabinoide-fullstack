package br.com.jotave.cannabis_calculator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para receber a pergunta do usuário destinada ao agente de IA.
 *
 * @param pergunta A pergunta do paciente sobre cannabis medicinal.
 */
public record ChatRequestDTO(

        @NotBlank(message = "A pergunta não pode estar vazia.")
        @Size(min = 3, max = 1000, message = "A pergunta deve ter entre 3 e 1000 caracteres.")
        String pergunta
) {}
