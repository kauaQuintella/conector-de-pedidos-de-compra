package com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kauaqpo.portifolio.conectorpedidoscompra.enums.ResultadoConferencia;

import java.util.List;
import java.util.UUID;

public record ConferenciaResponseDTO(
        @JsonProperty("id_conferencia") UUID idConferencia,
        @JsonProperty("id_pedido") UUID idPedido,
        @JsonProperty("resultado") ResultadoConferencia resultado,
        @JsonProperty("divergencias") List<DivergenciaDTO> divergencias
) {
}
