package com.kauaqpo.portifolio.conectorpedidoscompra.controller;

import com.kauaqpo.portifolio.conectorpedidoscompra.dto.gama.GamaItemLinhaDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.dto.gama.GamaPedidoDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Pedido;
import com.kauaqpo.portifolio.conectorpedidoscompra.ingestor.GamaIngestor;
import com.kauaqpo.portifolio.conectorpedidoscompra.parser.GamaPayloadAgrupador;
import com.kauaqpo.portifolio.conectorpedidoscompra.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Ingestão de pedidos do cliente Gama (JSON achatado, um objeto por item).
 */
@RestController
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class GamaController {

    private final GamaIngestor gamaIngestor;
    private final PedidoService pedidoService;

    @PostMapping("/gama")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, Object>> ingestirGama(
            @RequestBody List<@Valid GamaItemLinhaDTO> linhas
    ) {
        List<GamaPedidoDTO> pedidos = GamaPayloadAgrupador.agrupar(linhas);
        return pedidos.stream()
                .map(dto -> {
                    Pedido pedido = gamaIngestor.toEntity(dto);
                    Pedido salvo = pedidoService.upsert(pedido);
                    return Map.<String, Object>of(
                            "id_pedido", salvo.getId(),
                            "numero_pedido_origem", salvo.getNumeroPedidoOrigem(),
                            "cliente_origem", salvo.getClienteOrigem(),
                            "status", salvo.getStatus()
                    );
                })
                .toList();
    }
}
