package br.com.jotave.cannabis_calculator.dto.request;

import br.com.jotave.cannabis_calculator.model.ComposicaoCanabinoide;

import java.util.List;
/**
 * DTO para criar ou atualizar um produto.
 * @param nome Nome do produto (ex: "Óleo CBD Full Spectrum 2000mg").
 * @param volumeFrascoMl Volume total do frasco em mililitros.
 * @param gotasPorMl Estimativa de quantas gotas equivalem a 1ml.
 * @param composicoes Lista de canabinoides e suas concentrações.
 */
public record ProdutoRequestDTO(
        String nome,
        Integer volumeFrascoMl,
        Integer gotasPorMl,
        List<ComposicaoCanabinoide> composicoes) {
    /**
     * DTO aninhado para representar a composição de um único canabinoide.
     */
    public record ComposicaoRequestDTO(
            String nomeCanabinoide,
            Double concentracaoMgPorMl
    ) {}
}
