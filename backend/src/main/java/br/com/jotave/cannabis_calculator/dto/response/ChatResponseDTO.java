package br.com.jotave.cannabis_calculator.dto.response;

/**
 * DTO para retornar a resposta do agente de IA ao usuário.
 *
 * @param resposta A resposta do agente sobre o tema consultado.
 * @param fonte Indicação de que a resposta é fornecida por um agente de IA.
 */
public record ChatResponseDTO(

        String resposta,

        String fonte
) {
    public ChatResponseDTO(String resposta) {
        this(resposta, "Agente de IA - Cannabis Medicinal");
    }
}
