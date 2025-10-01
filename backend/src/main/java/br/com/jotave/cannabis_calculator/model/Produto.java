package br.com.jotave.cannabis_calculator.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "produto")
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(name = "volume_frasco_ml")
    private Integer volumeFrascoMl;

    @Column(name = "gotas_por_ml")
    private Integer gotasPorMl;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ComposicaoCanabinoide> composicoes;
}

