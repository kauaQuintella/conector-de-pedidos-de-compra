package com.kauaqpo.portifolio.conectorpedidoscompra.dto.conferencia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kauaqpo.portifolio.conectorpedidoscompra.enums.TipoDivergencia;

public record DivergenciaDTO(
        @JsonProperty("tipo") TipoDivergencia tipo,
        @JsonProperty("codigo_material") String codigoMaterial,
        @JsonProperty("valor_esperado") String valorEsperado,
        @JsonProperty("valor_recebido") String valorRecebido,
        @JsonProperty("descricao") String descricao
) {
}
