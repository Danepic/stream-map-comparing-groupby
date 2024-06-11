package br.com.danepic.dbc_test.services;

import br.com.danepic.dbc_test.clients.MockClient;
import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.CompraResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;
import br.com.danepic.dbc_test.models.CompraDetalheResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @InjectMocks
    private CompraService target;

    @Mock
    private MockClient mockClient;

    @Test
    void findSortByValorTotalAsc() {
        final ProdutoResponse produto = new ProdutoResponse(11, "Tinto", BigDecimal.valueOf(500), "2022", 2023);

        final Set<ClienteResponse> clienteCompras = new HashSet<>();
        clienteCompras.add(new ClienteResponse("Danilo", "41111111111", Arrays.asList(
                new CompraResponse(produto.codigo(), 2)
        )));
        clienteCompras.add(new ClienteResponse("Bárbara", "31111111111", Collections.singletonList(
                new CompraResponse(produto.codigo(), 1)
        )));

        final List<CompraDetalheResponse> expected = Arrays.asList(
                new CompraDetalheResponse(BigDecimal.valueOf(500), "Bárbara", "31111111111", 1, produto),
                new CompraDetalheResponse(BigDecimal.valueOf(1000), "Danilo", "41111111111", 2, produto)
        );

        doReturn(clienteCompras).when(mockClient).getClienteCompras();
        doReturn(Collections.singleton(produto)).when(mockClient).getProdutos();

        final List<CompraDetalheResponse> actual = target.findSortByValorTotalAsc();

        assertEquals(expected, actual);
    }

    @Test
    void findMaiorCompraDoAno() {
        final ProdutoResponse produto = new ProdutoResponse(11, "Tinto", BigDecimal.valueOf(500), "2022", 2023);

        final Set<ClienteResponse> clienteCompras = new HashSet<>();
        clienteCompras.add(new ClienteResponse("Danilo", "41111111111", Arrays.asList(
                new CompraResponse(produto.codigo(), 2)
        )));
        clienteCompras.add(new ClienteResponse("Bárbara", "31111111111", Collections.singletonList(
                new CompraResponse(produto.codigo(), 1)
        )));

        final CompraDetalheResponse expected = new CompraDetalheResponse(BigDecimal.valueOf(1000), "Danilo", "41111111111", 2, produto);

        doReturn(clienteCompras).when(mockClient).getClienteCompras();
        doReturn(Collections.singleton(produto)).when(mockClient).getProdutos();

        final CompraDetalheResponse actual = target.findMaiorCompraDoAno(2023);

        assertEquals(expected, actual);
    }
}