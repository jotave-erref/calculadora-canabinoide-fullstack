package br.com.jotave.cannabis_calculator.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
/**
 * DTO para receber os dados necessários para o cálculo da dosagem.
 * Inclui também a lista de medicamentos para a verificação de interação.
 *
 * @param produtoId O ID do produto selecionado.
 * @param numeroDeGotas A quantidade de gotas que o paciente irá usar.
 * @param medicamentosAtuais Uma lista com os nomes dos medicamentos que o paciente já usa.
 */
public record CalculoRequestDTO(

        @NotNull(message = "O ID do produto não pode ser nulo.")
        Long produtoId,

        @Positive(message = "O número de gotas deve ser maior que zero.")
        int numeroDeGotas,

        List<String> medicamentosAtuais
) {}
