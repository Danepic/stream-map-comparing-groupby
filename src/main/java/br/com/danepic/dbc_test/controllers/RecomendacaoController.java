package br.com.danepic.dbc_test.controllers;

import br.com.danepic.dbc_test.models.ClienteFidelidadeResponse;
import br.com.danepic.dbc_test.models.RecomendacaoResponse;
import br.com.danepic.dbc_test.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("recomendacao")
public class RecomendacaoController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("cliente/tipo")
    public List<RecomendacaoResponse> findRecomendacoesPorTipo() {
        return clienteService.findRecomendacoesPorTipo();
    }
}
