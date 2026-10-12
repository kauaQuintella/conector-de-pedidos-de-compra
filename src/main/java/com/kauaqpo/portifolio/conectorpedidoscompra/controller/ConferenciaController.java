package com.kauaqpo.portifolio.conectorpedidoscompra.controller;

import com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia.ConferenciaResponseDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia.NotaFiscalDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia.RelatorioConferenciasDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.service.ConferenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConferenciaController {
    private final ConferenciaService conferenciaService;

    @PostMapping("/notas-fiscais/conferir")
    public ConferenciaResponseDTO conferir(@Valid @RequestBody NotaFiscalDTO nota) {
        return conferenciaService.conferir(nota);
    }

    @GetMapping("/relatorios/conferencias")
    public RelatorioConferenciasDTO relatorio() {
        return conferenciaService.relatorio();
    }
}
