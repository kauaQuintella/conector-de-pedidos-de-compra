package com.kauaqpo.portifolio.conectorpedidoscompra.normalizer;

import com.kauaqpo.portifolio.conectorpedidoscompra.enums.StatusPedido;

/**
 * Contrato para tradução do status bruto de cada cliente para o enum único.
 * Cada cliente implementa sua própria versão.
 */
public interface StatusMapper {

    /**
     * Traduz o status no vocabulário do cliente para o enum.
     *
     * @param rawStatus string de status conforme o cliente envia
     * @return StatusPedido correspondente
     * @throws IllegalArgumentException se o status não for reconhecido
     */
    StatusPedido map(String rawStatus);
}
