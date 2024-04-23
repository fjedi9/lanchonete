package com.vepilef.food.api.v1.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.vepilef.food.api.v1.assembler.PedidoInputDisassembler;
import com.vepilef.food.api.v1.assembler.PedidoModelAssembler;
import com.vepilef.food.api.v1.assembler.PedidoResumoModelAssembler;
import com.vepilef.food.api.v1.model.PedidoModel;
import com.vepilef.food.api.v1.model.PedidoResumoModel;
import com.vepilef.food.api.v1.model.input.PedidoInput;
import com.vepilef.food.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.vepilef.food.core.data.PageWrapper;
import com.vepilef.food.core.data.PageableTranslator;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.core.security.FoodSecurity;
import com.vepilef.food.domain.exception.EntidadeNaoEncontradaException;
import com.vepilef.food.domain.exception.NegocioException;
import com.vepilef.food.domain.filter.PedidoFilter;
import com.vepilef.food.domain.model.Pedido;
import com.vepilef.food.domain.model.Usuario;
import com.vepilef.food.domain.service.CadastroProdutoService;
import com.vepilef.food.domain.service.EmissaoPedidoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    EmissaoPedidoService emissaoPedidoService;

    @Autowired
    CadastroProdutoService cadastroProdutoService;

    @Autowired
    PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    PedidoInputDisassembler pedidoInputDisassembler;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @Autowired
    private FoodSecurity foodSecurity;

    @CheckSecurity.Pedidos.PodePesquisar
    @Override
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, @PageableDefault(size = 10) Pageable pageable) {

        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = emissaoPedidoService.findAll(filtro, pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }

    @Override
    @GetMapping("/filter")
    public MappingJacksonValue listarPedidosFilter(@RequestParam(required = false) String campos) {
        List<Pedido> pedidos = emissaoPedidoService.findAll();
        CollectionModel<PedidoResumoModel> pedidosModel = pedidoResumoModelAssembler.toCollectionModel(pedidos);
        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();

        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        if (StringUtils.isNotBlank(campos)) {
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }

        pedidosWrapper.setFilters(filterProvider);
        return pedidosWrapper;
    }

    @CheckSecurity.Pedidos.PodeBuscar
    @Override
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        log.info(">> Authorities do usuario: " + String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
        Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);

        return pedidoModelAssembler.toModel(pedido);
    }

    @CheckSecurity.Pedidos.PodeCriar
    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);


            novoPedido.setCliente(new Usuario());

            novoPedido.getCliente().setId(foodSecurity.getUsuarioId());

            novoPedido = emissaoPedidoService.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable apiPageable) {

        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "valorTotal", "valorTotal",
                "dataCriacao", "dataCriacao",
                "nomeRestaurante", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "nomeCliente", "cliente.nome"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }

}
