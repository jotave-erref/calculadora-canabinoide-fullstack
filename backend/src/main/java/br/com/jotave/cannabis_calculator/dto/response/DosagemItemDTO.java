package br.com.jotave.cannabis_calculator.dto.response;

/**
 * Representa a dosagem calculada para um único canabinoide.
 * @param nomeCanabinoide Nome do canabinoide (ex: "CBD", "THC").
 * @param miligramas A quantidade total em miligramas para a dosagem de gotas informada.
 */
public record DosagemItemDTO (
        String nomeCanabinoide,
        double miligramas
) {}
