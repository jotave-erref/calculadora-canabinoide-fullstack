package br.com.jotave.cannabis_calculator.config;

import br.com.jotave.cannabis_calculator.exception.OpenAIException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Configuração global do OpenFeign.
 * Define error handling para todos os clientes Feign.
 */
@Configuration
@Slf4j
public class FeignGlobalConfig {

    /**
     * Decoder customizado para tratar erros HTTP genericamente.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String body = extractBody(response);
            int status = response.status();

            if (status == 401) {
                log.error("Erro de autenticação: credenciais inválidas ou expiradas.");
                return new OpenAIException("Autenticação falhou: credenciais inválidas ou expiradas.");
            }

            if (status == 429) {
                log.warn("Rate limit atingido. Aguarde antes de fazer nova requisição.");
                return new OpenAIException("Rate limit atingido. Tente novamente em alguns minutos.");
            }

            if (status == 500 || status == 503) {
                log.error("Servidor indisponível: {}", status);
                return new OpenAIException("Servidor indisponível. Tente novamente mais tarde.");
            }

            log.error("Erro HTTP [{}]: {}", status, body);
            return new OpenAIException("Erro na comunicação: " + body);
        };
    }

    /**
     * Extrai o corpo da resposta de erro para logging.
     */
    private String extractBody(Response response) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
            return br.readLine();
        } catch (IOException e) {
            return "Não foi possível extrair o corpo da resposta";
        }
    }
}
