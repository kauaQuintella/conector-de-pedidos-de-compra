package com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record RelatorioConferenciasDTO(
        @JsonProperty("total") long total,
        @JsonProperty("por_resultado") Map<String, Long> porResultado,
        @JsonProperty("por_tipo") Map<String, Long> porTipo
) {
}
