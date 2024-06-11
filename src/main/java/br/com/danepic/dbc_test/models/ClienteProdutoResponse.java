package br.com.danepic.dbc_test.models;

import br.com.danepic.dbc_test.clients.models.ProdutoResponse;

import java.util.List;

public record ClienteProdutoResponse(String nome, String cpf, List<ProdutoResponse> produtos) {
}
