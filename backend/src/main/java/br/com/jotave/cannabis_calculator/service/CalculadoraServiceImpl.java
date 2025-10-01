package br.com.jotave.cannabis_calculator.service;

import br.com.jotave.cannabis_calculator.dto.request.CalculoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.request.ProdutoRequestDTO;
import br.com.jotave.cannabis_calculator.dto.response.CalculoResponseDTO;
import br.com.jotave.cannabis_calculator.dto.response.DosagemItemDTO;
import br.com.jotave.cannabis_calculator.dto.response.ProdutoResponseDTO;
import br.com.jotave.cannabis_calculator.exception.ProdutoNaoEncontradoException;
import br.com.jotave.cannabis_calculator.model.ComposicaoCanabinoide;
import br.com.jotave.cannabis_calculator.model.Produto;
import br.com.jotave.cannabis_calculator.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculadoraServiceImpl implements CalculadoraService {

    private final ProdutoRepository produtoRepository;


    public CalculadoraServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public CalculoResponseDTO calcularDosagemEInteracao(CalculoRequestDTO requestDTO) {
        // Busca o produto no banco ou lança uma exceção se não encontrar.
        Produto produto = produtoRepository.findById(requestDTO.produtoId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com ID " + requestDTO.produtoId() + " não encontrado."));

        // Calcula as dosagens
        List<DosagemItemDTO> dosagens = new ArrayList<>();
        for (ComposicaoCanabinoide composicao : produto.getComposicoes()) {
            double mgPorGota = composicao.getConcentracaoMgPorMl() / produto.getGotasPorMl();
            double dosagemTotal = mgPorGota * requestDTO.numeroDeGotas();

            // Arredondar para 2 casas decimais para uma melhor apresentação
            dosagemTotal = Math.round(dosagemTotal * 100.0) / 100.0;

            dosagens.add(new DosagemItemDTO(composicao.getNomeCanabinoide(), dosagemTotal));
        }

        // Verifica as interações medicamentosas
        String avisoInteracao = verificarInteracoes(produto, requestDTO.medicamentosAtuais());

        // Monta e retorna o DTO de resposta completo
        return new CalculoResponseDTO(dosagens, avisoInteracao);
    }

    @Override
    public List<ProdutoResponseDTO> listarTodosOsProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::toProdutoResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoResponseDTO cadastrarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = toProdutoEntity(produtoRequestDTO);
        Produto produtoSalvo = produtoRepository.save(produto);
        return toProdutoResponseDTO(produtoSalvo);
    }

    // --- MÉTODOS PRIVADOS AUXILIARES (HELPERS) ---

    // Lógica MOCK (simulada) para verificar interações medicamentosas.
    private String verificarInteracoes(Produto produto, List<String> medicamentos) {
        if (medicamentos == null || medicamentos.isEmpty()) {
            return "Nenhum medicamento informado para verificação.";
        }

        StringBuilder avisos = new StringBuilder();
        boolean temCBD = produto.getComposicoes().stream()
                .anyMatch(c -> "CBD".equalsIgnoreCase(c.getNomeCanabinoide()));

        for (String medicamento : medicamentos) {
            if (temCBD && "Varfarina".equalsIgnoreCase(medicamento.trim())) {
                avisos.append("ALERTA: O CBD pode aumentar a concentração de Varfarina no sangue, elevando o risco de sangramento. ");
            }
            if ("Clobazam".equalsIgnoreCase(medicamento.trim())) {
                avisos.append("ATENÇÃO: O uso concomitante com canabinoides pode aumentar a sonolência e os efeitos sedativos. ");
            }
            if ("Losartana".equalsIgnoreCase(medicamento.trim())) {
                avisos.append("ATENÇÃO: O uso concomitante com canabinoides pode aumentar a sonolência e os efeitos sedativos. ");
            }
        }

        return avisos.length() > 0 ? avisos.toString().trim() : "Nenhuma interação crítica conhecida encontrada.";
    }


    private ProdutoResponseDTO toProdutoResponseDTO(Produto produto) {
        List<ProdutoResponseDTO.ComposicaoResponseDTO> composicoesDTO = produto.getComposicoes().stream()
                .map(comp -> new ProdutoResponseDTO.ComposicaoResponseDTO(
                        comp.getNomeCanabinoide(),
                        comp.getConcentracaoMgPorMl()))
                .collect(Collectors.toList());

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getVolumeFrascoMl(),
                composicoesDTO);
    }


    private Produto toProdutoEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setVolumeFrascoMl(dto.volumeFrascoMl());
        produto.setGotasPorMl(dto.gotasPorMl());

        List<ComposicaoCanabinoide> composicoes = dto.composicoes().stream()
                .map(compDTO -> {
                    ComposicaoCanabinoide composicao = new ComposicaoCanabinoide();
                    composicao.setNomeCanabinoide(compDTO.getNomeCanabinoide());
                    composicao.setConcentracaoMgPorMl(compDTO.getConcentracaoMgPorMl());
                    composicao.setProduto(produto);
                    return composicao;
                })
                .collect(Collectors.toList());

        produto.setComposicoes(composicoes);
        return produto;
    }
}