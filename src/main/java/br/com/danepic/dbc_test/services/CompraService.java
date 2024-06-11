package br.com.danepic.dbc_test.services;

import br.com.danepic.dbc_test.clients.MockClient;
import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.CompraResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;
import br.com.danepic.dbc_test.models.CompraDetalheResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompraService {

    @Autowired
    private MockClient mockClient;

    public List<CompraDetalheResponse> findSortByValorTotalAsc() {
        final List<CompraDetalheResponse> comprasDetalhe = this.find(null);
        comprasDetalhe.sort(this.ordenarPorValorTotal());
        return comprasDetalhe;
    }

    public CompraDetalheResponse findMaiorCompraDoAno(Integer ano) {
        final List<CompraDetalheResponse> comprasDetalhe = this.find(ano);
        comprasDetalhe.sort(this.ordenarPorValorTotal().reversed());
        return comprasDetalhe.stream().findFirst().orElseThrow(() -> new RuntimeException("Compra não encontrada para o ano!"));
    }

    private List<CompraDetalheResponse> find(Integer ano) {
        final Set<ClienteResponse> clientes = mockClient.getClienteCompras();
        final Map<Integer, ProdutoResponse> produtos = mockClient.getProdutos().stream()
                .filter(produto -> Optional.ofNullable(ano).isEmpty() || produto.anoCompra().equals(ano))
                .collect(Collectors.toMap(ProdutoResponse::codigo, Function.identity()));

        final List<CompraDetalheResponse> comprasDetalhe = new ArrayList<>();

        for (ClienteResponse cliente : clientes) {
            final List<CompraResponse> compras = cliente.compras();
            for (CompraResponse compra : compras) {
                if (produtos.containsKey(compra.codigo())) {
                    final ProdutoResponse produtoFind = produtos.get(compra.codigo());
                    comprasDetalhe.add(this.buildCompraDetalhe(cliente, compra, produtoFind));
                }
            }
        }

        return comprasDetalhe;
    }

    private Comparator<CompraDetalheResponse> ordenarPorValorTotal() {
        return Comparator.comparing(CompraDetalheResponse::valorTotal);
    }

    private CompraDetalheResponse buildCompraDetalhe(ClienteResponse cliente, CompraResponse compra, ProdutoResponse produtoFind) {
        return new CompraDetalheResponse(produtoFind.preco().multiply(BigDecimal.valueOf(compra.quantidade())),
                cliente.nome(), cliente.cpf(), compra.quantidade(), produtoFind);
    }
}
