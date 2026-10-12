package com.kauaqpo.portifolio.conectorpedidoscompra.dto.erro;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErroRespostaDTO(
        @JsonProperty("status") int status,
        @JsonProperty("mensagem") String mensagem
) {
}
