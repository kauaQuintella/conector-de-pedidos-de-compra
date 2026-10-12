package com.kauaqpo.portifolio.conectorpedidoscompra.ingestor;

import com.kauaqpo.portifolio.conectorpedidoscompra.dto.gama.GamaItemLinhaDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.dto.gama.GamaPedidoDTO;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Fornecedor;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Item;
import com.kauaqpo.portifolio.conectorpedidoscompra.entity.Pedido;
import com.kauaqpo.portifolio.conectorpedidoscompra.ingestor.strategy.PedidoIngestor;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.CnpjSanitizer;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.GamaDateParser;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.GamaStatusMapper;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.StringSanitizer;
import com.kauaqpo.portifolio.conectorpedidoscompra.normalizer.UnitConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ingestor do Gama Logística. Recebe {@link GamaPedidoDTO} já agrupado.
 * Persistência fica no {@code PedidoService}.
 */
@Component
@RequiredArgsConstructor
public class GamaIngestor implements PedidoIngestor<GamaPedidoDTO> {

    private static final String CLIENTE_ORIGEM = "GAMA";
    private static final String MOEDA = "BRL";
    private static final String UNIDADE_NOTA = "UN";

    private final GamaStatusMapper statusMapper;

    @Override
    public Pedido toEntity(GamaPedidoDTO dto) {
        Fornecedor fornecedor = mapFornecedor(dto);
        Pedido pedido = mapPedido(dto, fornecedor);
        List<Item> itens = mapItens(dto, pedido);
        pedido.setItens(itens);
        return pedido;
    }

    private Fornecedor mapFornecedor(GamaPedidoDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(CnpjSanitizer.sanitize(dto.cnpjFornecedor()));
        fornecedor.setNome(StringSanitizer.sanitize(dto.nomeFornecedor()));
        return fornecedor;
    }

    private Pedido mapPedido(GamaPedidoDTO dto, Fornecedor fornecedor) {
        Pedido pedido = new Pedido();
        pedido.setNumeroPedidoOrigem(dto.ped());
        pedido.setClienteOrigem(CLIENTE_ORIGEM);
        pedido.setDataCriacao(GamaDateParser.parse(dto.dtCriacao()));
        pedido.setStatus(statusMapper.map(dto.situacao()));
        pedido.setMoeda(MOEDA);
        pedido.setFornecedor(fornecedor);
        return pedido;
    }

    private List<Item> mapItens(GamaPedidoDTO dto, Pedido pedido) {
        return dto.itens().stream()
                .map(linha -> mapItem(linha, pedido))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Item mapItem(GamaItemLinhaDTO linha, Pedido pedido) {
        Item item = new Item();
        item.setPedido(pedido);
        item.setLinha(String.valueOf(linha.item()));
        item.setCodigoMaterial(linha.codMat());
        item.setDescricao(StringSanitizer.sanitize(linha.descMat()));
        item.setUnidadeMedida(UNIDADE_NOTA);
        item.setQuantidadePedida(UnitConverter.converterQuantidade(linha.qtdPed(), linha.fatorConv()));
        item.setQuantidadeRecebida(UnitConverter.converterQuantidade(linha.qtdRec(), linha.fatorConv()));
        item.setPrecoUnitario(UnitConverter.converterPrecoUnitario(linha.precoUnitCentavos(), linha.fatorConv()));
        return item;
    }
}
