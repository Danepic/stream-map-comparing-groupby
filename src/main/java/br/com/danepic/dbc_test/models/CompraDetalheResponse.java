package br.com.danepic.dbc_test.models;

import br.com.danepic.dbc_test.clients.models.ProdutoResponse;

import java.math.BigDecimal;

public record CompraDetalheResponse(BigDecimal valorTotal, String nome, String cpf, Integer quantidade, ProdutoResponse produto) {
}
