package com.kauaqpo.portifolio.conectorpedidoscompra.ingestor;

import com.kauaqpo.portifolio.conectorpedidoscompra.dto.alfa.AlfaItemDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.dto.alfa.AlfaPedidoDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Fornecedor;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Item;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Pedido;
import com.kauaqpo.portifolio.conectorpedidoscompra.ingestor.strategy.PedidoIngestor;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.AlfaStatusMapper;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.CnpjSanitizer;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.StringSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.AlfaDateParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ingestor para o cliente Alfa (JSON aninhado).
 *
 * Traduz AlfaPedidoDTO → Pedido (transiente). Aplica:
 * - CnpjSanitizer para remover máscara do CNPJ
 * - AlfaStatusMapper para traduzir o status para o enum
 *
 * Não acessa o banco — persistência é responsabilidade do PedidoService.
 * clienteOrigem fixo: "ALFA".
 */
@Component
@RequiredArgsConstructor
public class AlfaIngestor implements PedidoIngestor<AlfaPedidoDTO> {

    private static final String CLIENTE_ORIGEM = "ALFA";

    private final AlfaStatusMapper statusMapper;

    @Override
    public Pedido toEntity(AlfaPedidoDTO dto) {
        Fornecedor fornecedor = mapFornecedor(dto);
        Pedido pedido = mapPedido(dto, fornecedor);
        List<Item> itens = mapItens(dto, pedido);
        pedido.setItens(itens);
        return pedido;
    }

    private Fornecedor mapFornecedor(AlfaPedidoDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(CnpjSanitizer.sanitize(dto.fornecedor().cnpj()));
        fornecedor.setNome(StringSanitizer.sanitize(dto.fornecedor().nome()));
        return fornecedor;
    }

    private Pedido mapPedido(AlfaPedidoDTO dto, Fornecedor fornecedor) {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedidoOrigem(dto.numeroPedido());
        pedido.setClienteOrigem(CLIENTE_ORIGEM);
        pedido.setDataCriacao(AlfaDateParser.parse(dto.dataCriacao()));
        pedido.setStatus(statusMapper.map(dto.status()));
        pedido.setMoeda(dto.moeda());
        pedido.setFornecedor(fornecedor);
        // dataIngestao é preenchida pelo PedidoService no momento do upsert
        return pedido;
    }

    private List<Item> mapItens(AlfaPedidoDTO dto, Pedido pedido) {
        return dto.itens().stream()
                .map(itemDTO -> mapItem(itemDTO, pedido))
                .collect(Collectors.toCollection(ArrayList::new)); // Retorna uma ArrayList mutável
    }

    private Item mapItem(AlfaItemDTO dto, Pedido pedido) {
        Item item = new Item();
        item.setPedido(pedido);
        item.setLinha(dto.linha());
        item.setCodigoMaterial(dto.codigoMaterial());
        item.setDescricao(StringSanitizer.sanitize(dto.descricao()));
        item.setUnidadeMedida(dto.unidadeMedida());
        item.setQuantidadePedida(dto.quantidadePedida());
        item.setQuantidadeRecebida(dto.quantidadeRecebida());
        item.setPrecoUnitario(dto.precoUnitario());
        // quantidadePendente é GENERATED ALWAYS no Postgres — não é setada aqui
        return item;
    }
}
