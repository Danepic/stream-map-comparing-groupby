package br.com.danepic.dbc_test.models;

import java.math.BigDecimal;

public record ClienteFidelidadeResponse(String nome, String cpf, Integer quantidadeDeCompras,
                                        BigDecimal valorTotalGastoEmCompras) {
}
