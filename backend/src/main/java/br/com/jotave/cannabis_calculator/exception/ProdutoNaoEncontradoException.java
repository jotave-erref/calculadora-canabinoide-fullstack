package br.com.jotave.cannabis_calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma operação tenta acessar um Produto que não existe no banco de dados.
 * A anotação @ResponseStatus instrui o Spring a retornar um status HTTP 404 (Not Found)
 * sempre que esta exceção não for capturada e chegar à camada do controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
