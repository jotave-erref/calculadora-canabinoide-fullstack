package br.com.jotave.cannabis_calculator.dto.response;

import java.util.List;

/**
 * DTO para exibir as informações de um produto para o cliente.
 * Este é o "contrato" de como um produto é apresentado pela API.
 * @param id O ID do produto, útil para fazer outras requisições.
 * @param nome O nome do produto.
 * @param volumeFrascoMl O volume do frasco.
 * @param composicoes A lista detalhada de canabinoides.
 */
public record ProdutoResponseDTO(
        Long id,
        String nome,
        Integer volumeFrascoMl,
        List<ComposicaoResponseDTO> composicoes
) {
    /**
     * DTO aninhado para exibir a composição de um canabinoide.
     */
    public record ComposicaoResponseDTO(
            String nomeCanabinoide,
            Double concentracaoMgPorMl
    ) {}
}
