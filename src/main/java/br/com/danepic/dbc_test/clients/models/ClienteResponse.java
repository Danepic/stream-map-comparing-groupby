package br.com.danepic.dbc_test.clients.models;

import java.util.List;

public record ClienteResponse(String nome, String cpf, List<CompraResponse> compras) {
}
