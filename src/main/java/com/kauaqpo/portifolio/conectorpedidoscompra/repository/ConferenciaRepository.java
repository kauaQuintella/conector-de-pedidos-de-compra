package com.kauaqpo.portifolio.conectorpedidoscompra.repository;

import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Conferencia;
import com.kauaqpo.portifolio.conectorpedidoscompra.enums.ResultadoConferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConferenciaRepository extends JpaRepository<Conferencia, UUID> {
    long countByResultado(ResultadoConferencia resultado);
}
