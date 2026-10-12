package com.kauaqpo.portifolio.conectorpedidoscompra.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kauaqpo.portifolio.conectorpedidoscompra.enums.StatusPedido;

import java.time.Instant;
import java.util.UUID;

public record PedidoResumoResponseDTO(
        @JsonProperty("id_pedido") UUID idPedido,
        @JsonProperty("numero_pedido") String numeroPedido,
        @JsonProperty("cliente_origem") String clienteOrigem,
        @JsonProperty("status") StatusPedido status,
        @JsonProperty("data_criacao") Instant dataCriacao,
        @JsonProperty("moeda") String moeda,
        @JsonProperty("fornecedor") FornecedorResponseDTO fornecedor
) {
}
