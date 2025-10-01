package br.com.jotave.cannabis_calculator.repository;

import br.com.jotave.cannabis_calculator.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
