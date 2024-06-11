package br.com.danepic.dbc_test.models;

import br.com.danepic.dbc_test.clients.models.ProdutoResponse;

import java.util.List;

public record RecomendacaoResponse(String nome, List<ProdutoResponse> recomendacoes) {
}
