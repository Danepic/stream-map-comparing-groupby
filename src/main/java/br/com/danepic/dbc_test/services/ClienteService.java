package br.com.danepic.dbc_test.services;

import br.com.danepic.dbc_test.clients.MockClient;
import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.CompraResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;
import br.com.danepic.dbc_test.models.ClienteFidelidadeResponse;
import br.com.danepic.dbc_test.models.ClienteProdutoResponse;
import br.com.danepic.dbc_test.models.CompraDetalheResponse;
import br.com.danepic.dbc_test.models.RecomendacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private MockClient mockClient;

    @Autowired
    private CompraService compraService;

    public List<ClienteFidelidadeResponse> findFieis() {
        final List<CompraDetalheResponse> comprasDetalhe = compraService.findSortByValorTotalAsc();

        final Map<String, List<CompraDetalheResponse>> comprasAgrupadasPorCliente = comprasDetalhe.stream()
                .collect(Collectors.groupingBy(CompraDetalheResponse::cpf));

        final List<ClienteFidelidadeResponse> clientesFidelidade = new ArrayList<>();

        for (Map.Entry<String, List<CompraDetalheResponse>> compraAgrupadaPorCliente : comprasAgrupadasPorCliente.entrySet()) {
            final BigDecimal valorSomadoEmTodasCompras = compraAgrupadaPorCliente.getValue().stream().map(CompraDetalheResponse::valorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            final CompraDetalheResponse dadosBasicos = compraAgrupadaPorCliente.getValue().stream().findAny()
                    .orElseThrow(() -> new RuntimeException("Cliente não tem compras!"));

            clientesFidelidade.add(new ClienteFidelidadeResponse(dadosBasicos.nome(), dadosBasicos.cpf(),
                    compraAgrupadaPorCliente.getValue().size(), valorSomadoEmTodasCompras));
        }

        clientesFidelidade.sort(Comparator.comparing(ClienteFidelidadeResponse::quantidadeDeCompras)
                .thenComparing(ClienteFidelidadeResponse::valorTotalGastoEmCompras).reversed());

        return clientesFidelidade;
    }

    public List<RecomendacaoResponse> findRecomendacoesPorTipo() {
        final Set<ClienteResponse> clienteCompras = mockClient.getClienteCompras();

        final Map<Integer, ProdutoResponse> produtos = mockClient.getProdutos().stream()
                .collect(Collectors.toMap(ProdutoResponse::codigo, Function.identity()));

        final List<ClienteProdutoResponse> clientesProdutos = clienteCompras.stream().map(cliente -> new ClienteProdutoResponse(cliente.nome(), cliente.cpf(),
                cliente.compras().stream().filter(compra -> produtos.containsKey(compra.codigo()))
                        .map(compra -> produtos.get(compra.codigo()))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList());

        return clientesProdutos.stream().map(clienteProduto -> {
            final String tipoFavorito = clienteProduto.produtos().stream()
                    .collect(Collectors.groupingBy(ProdutoResponse::tipoVinho, Collectors.counting())).entrySet()
                    .stream().max(Map.Entry.comparingByValue()).orElseThrow(RuntimeException::new).getKey();

            return new RecomendacaoResponse(clienteProduto.nome(), produtos.values().stream()
                    .filter(produto -> produto.tipoVinho().equals(tipoFavorito)).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }
}
