package br.com.danepic.dbc_test.services;

import br.com.danepic.dbc_test.clients.MockClient;
import br.com.danepic.dbc_test.clients.models.ClienteResponse;
import br.com.danepic.dbc_test.clients.models.CompraResponse;
import br.com.danepic.dbc_test.clients.models.ProdutoResponse;
import br.com.danepic.dbc_test.models.ClienteFidelidadeResponse;
import br.com.danepic.dbc_test.models.CompraDetalheResponse;
import br.com.danepic.dbc_test.models.RecomendacaoResponse;
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
class ClienteServiceTest {

    @InjectMocks
    private ClienteService target;

    @Mock
    private MockClient mockClient;

    @Mock
    private CompraService compraService;

    @Test
    void findFieis() {
        final ProdutoResponse produto = new ProdutoResponse(11, "Tinto", BigDecimal.valueOf(500), "2022", 2023);

        final List<CompraDetalheResponse> comprasDetalhe = Arrays.asList(
                new CompraDetalheResponse(BigDecimal.valueOf(1000), "Danilo", "41111111111", 2, produto),
                new CompraDetalheResponse(BigDecimal.valueOf(1000), "Danilo", "41111111111", 2, produto),
                new CompraDetalheResponse(BigDecimal.valueOf(500), "Bárbara", "31111111111", 1, produto)
        );

        final List<ClienteFidelidadeResponse> expected = Arrays.asList(
                new ClienteFidelidadeResponse("Danilo", "41111111111", 2, BigDecimal.valueOf(2000)),
                new ClienteFidelidadeResponse("Bárbara", "31111111111", 1, BigDecimal.valueOf(500))
        );

        doReturn(comprasDetalhe).when(compraService).findSortByValorTotalAsc();

        final List<ClienteFidelidadeResponse> actual = target.findFieis();

        assertEquals(expected, actual);
    }

    @Test
    void findRecomendacoesPorTipo() {
        final ProdutoResponse produto = new ProdutoResponse(11, "Tinto", BigDecimal.valueOf(500), "2022", 2023);

        final Set<ClienteResponse> clienteCompras = new HashSet<>();
        clienteCompras.add(new ClienteResponse("Danilo", "41111111111", Arrays.asList(
                new CompraResponse(produto.codigo(), 2),
                new CompraResponse(produto.codigo(), 1)
        )));
        clienteCompras.add(new ClienteResponse("Bárbara", "31111111111", Collections.singletonList(
                new CompraResponse(produto.codigo(), 1)
        )));

        final List<RecomendacaoResponse> expected = Arrays.asList(
                new RecomendacaoResponse("Danilo", Collections.singletonList(produto)),
                new RecomendacaoResponse("Bárbara", Collections.singletonList(produto))
        );

        doReturn(clienteCompras).when(mockClient).getClienteCompras();
        doReturn(Collections.singleton(produto)).when(mockClient).getProdutos();

        final List<RecomendacaoResponse> actual = target.findRecomendacoesPorTipo();

        assertEquals(expected, actual);
    }
}