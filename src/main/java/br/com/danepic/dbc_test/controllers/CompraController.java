package br.com.danepic.dbc_test.controllers;

import br.com.danepic.dbc_test.models.CompraDetalheResponse;
import br.com.danepic.dbc_test.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping
    public List<CompraDetalheResponse> find() {
        return compraService.findSortByValorTotalAsc();
    }

    @GetMapping("maior-compra/{ano}")
    public CompraDetalheResponse findMaiorCompraDoAno(@PathVariable Integer ano) {
        return compraService.findMaiorCompraDoAno(ano);
    }
}
