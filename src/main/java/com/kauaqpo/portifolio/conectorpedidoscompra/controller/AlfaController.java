package com.kauaqpo.portifolio.conectorpedidoscompra.controller;

import com.kauaqpo.portifolio.conectorpedidoscompra.dto.alfa.AlfaPayloadDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Pedido;
import com.kauaqpo.portifolio.conectorpedidoscompra.ingestor.AlfaIngestor;
import com.kauaqpo.portifolio.conectorpedidoscompra.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoint de ingestão de pedidos do cliente Alfa.
 *
 * Rota dedicada por cliente — o Spring resolve o AlfaIngestor via injeção de dependência,
 * tornando o Factory formal desnecessário neste desenho.
 */
@RestController
@RequestMapping("/ingest")
@RequiredArgsConstructor
public class AlfaController {

    private final AlfaIngestor alfaIngestor;
    private final PedidoService pedidoService;

    /**
     * Recebe um payload do cliente Alfa contendo uma lista de pedidos de compra,
     * traduz cada pedido via AlfaIngestor e persiste via PedidoService (upsert).
     *
     * @param payload payload validado pelo Bean Validation, contendo a lista purchase_orders
     * @return lista de resultados, um por pedido processado
     */
    @PostMapping("/alfa")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, Object>> ingestirAlfa(@RequestBody @Valid AlfaPayloadDTO payload) {
        return payload.purchaseOrders().stream()
                .map(dto -> {
                    Pedido pedido = alfaIngestor.toEntity(dto);
                    Pedido salvo = pedidoService.upsert(pedido);
                    return Map.<String, Object>of(
                            "id_pedido",            salvo.getId(),
                            "numero_pedido_origem",  salvo.getNumeroPedidoOrigem(),
                            "cliente_origem",        salvo.getClienteOrigem(),
                            "status",                salvo.getStatus()
                    );
                })
                .toList();
    }
}
