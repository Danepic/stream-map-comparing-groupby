package br.com.danepic.dbc_test.clients.impl;

import br.com.danepic.dbc_test.clients.MockClient;
import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class MockClientImpl implements MockClient {

    @Value("classpath:mock/cliente-compra-mock.json")
    private Resource clienteCompraFile;

    @Value("classpath:mock/produto-mock.json")
    private Resource produtoFile;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Set<ProdutoResponse> getProdutos() {
        try {
            return objectMapper.readValue(produtoFile.getContentAsByteArray(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao tentar obter mock de produtos!");
        }
    }

    @Override
    public Set<ClienteResponse> getClienteCompras() {
        try {
            return objectMapper.readValue(clienteCompraFile.getContentAsByteArray(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao tentar obter mock de clientes/compras!");
        }
    }
}
