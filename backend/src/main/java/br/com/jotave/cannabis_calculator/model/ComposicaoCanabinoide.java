package br.com.jotave.cannabis_calculator.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "composicao_canabinoide")
@Data
public class ComposicaoCanabinoide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_canabinoide")
    private String nomeCanabinoide;
    @Column(name = "concentracao_mg_por_ml")
    private Double concentracaoMgPorMl;

    @ManyToOne // Muitas composições pertencem a um produto
    @JoinColumn(name = "produto_id") // A coluna no banco que fará a ligação
    private Produto produto;
}
