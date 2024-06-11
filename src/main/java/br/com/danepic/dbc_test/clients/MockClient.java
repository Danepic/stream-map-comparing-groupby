package br.com.danepic.dbc_test.clients;

import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;

import java.util.Set;

public interface MockClient {

    Set<ProdutoResponse> getProdutos();

    Set<ClienteResponse> getClienteCompras();

}
