package br.com.jotave.cannabis_calculator.controller;

import br.com.jotave.cannabis_calculator.dto.request.CalculoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.request.ProdutoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.response.CalculoResponseDTO;
import br.com.jotave.cannabis_calculator.dto.response.ProdutoResponseDTO;
import br.com.jotave.cannabis_calculator.service.CalculadoraService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calculadora")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CalculadoraController {

    private final CalculadoraService calculadoraService;


    public CalculadoraController(CalculadoraService calculadoraService) {
        this.calculadoraService = calculadoraService;
    }

    /**
     * Endpoint para calcular a dosagem e verificar interações.
     * HTTP Method: POST
     * URL: /api/v1/calculadora/calcular
     */
    @PostMapping("/calcular")
    public ResponseEntity<CalculoResponseDTO> calcularDosagem(@Valid @RequestBody CalculoRequestDTO requestDTO) {
        log.info("Recebida requisição para calcular dosagem para o produto ID: {}", requestDTO.produtoId());
        CalculoResponseDTO response = calculadoraService.calcularDosagemEInteracao(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para listar todos os produtos cadastrados.
     * HTTP Method: GET
     * URL: /api/v1/calculadora/produtos
     */
    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos() {
        log.info("Recebida requisição para listar todos os produtos.");
        List<ProdutoResponseDTO> produtos = calculadoraService.listarTodosOsProdutos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Endpoint para cadastrar um novo produto.
     * HTTP Method: POST
     * URL: /api/v1/calculadora/produtos
     */
    @PostMapping("/produtos")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(
            @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        log.info("Recebida requisição para cadastrar novo produto: {}", produtoRequestDTO.nome());
        ProdutoResponseDTO produtoCriado = calculadoraService.cadastrarProduto(produtoRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }
}
