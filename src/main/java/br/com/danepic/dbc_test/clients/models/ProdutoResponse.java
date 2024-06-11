package br.com.danepic.dbc_test.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProdutoResponse(Integer codigo, @JsonProperty("tipo_vinho") String tipoVinho, BigDecimal preco,
                              String safra, @JsonProperty("ano_compra") Integer anoCompra) {
}
