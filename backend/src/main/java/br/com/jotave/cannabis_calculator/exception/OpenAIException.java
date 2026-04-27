package br.com.jotave.cannabis_calculator.exception;

/**
 * Exceção lançada quando há erro na comunicação com a API OpenAI.
 */
public class OpenAIException extends RuntimeException {

    public OpenAIException(String message) {
        super(message);
    }

    public OpenAIException(String message, Throwable cause) {
        super(message, cause);
    }
}
