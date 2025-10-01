package br.com.jotave.cannabis_calculator.service;

import br.com.jotave.cannabis_calculator.dto.request.CalculoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.request.ProdutoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.response.CalculoResponseDTO;
import br.com.jotave.cannabis_calculator.dto.response.ProdutoResponseDTO;

import java.util.List;

/**
 * Interface que define o contrato para os serviços de negócio da calculadora.
 * Abstrai a implementação das regras de negócio.
 */
public interface CalculadoraService {

    /**
     * Calcula a dosagem em miligramas para cada canabinoide de um produto
     * e verifica possíveis interações medicamentosas.
     *
     * @param requestDTO DTO contendo o ID do produto, número de gotas e medicamentos atuais.
     * @return um DTO com o resultado do cálculo e os avisos de interação.
     */
    CalculoResponseDTO calcularDosagemEInteracao(CalculoRequestDTO requestDTO);

    /**
     * Lista todos os produtos disponíveis no sistema.
     *
     * @return Uma lista de DTOs representando os produtos.
     */
    List<ProdutoResponseDTO> listarTodosOsProdutos();

    /**
     * Cadastra um novo produto no sistema.
     *
     * @param produtoRequestDTO DTO com as informações do novo produto.
     * @return o DTO do produto recém-criado, incluindo seu ID gerado.
     */
    ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO);

}
