package br.com.jotave.cannabis_calculator.dto.response;

import java.util.List;

/**
 * DTO que representa o resultado completo do cálculo de dosagem e verificação de interação.
 * @param dosagens A lista de dosagens para cada canabinoide presente no produto.
 * @param avisoInteracao Uma string contendo o resultado da verificação de interação medicamentosa.
 */
public record CalculoResponseDTO(
        List<DosagemItemDTO> dosagens,
        String avisoInteracao
) {}
