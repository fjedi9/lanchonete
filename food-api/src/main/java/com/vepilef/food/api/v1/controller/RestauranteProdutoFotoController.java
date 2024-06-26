package com.vepilef.food.api.v1.controller;

import com.vepilef.food.api.v1.assembler.FotoProdutoModelAssembler;
import com.vepilef.food.api.v1.model.FotoProdutoModel;
import com.vepilef.food.api.v1.model.input.FotoProdutoInput;
import com.vepilef.food.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.vepilef.food.core.security.CheckSecurity;
import com.vepilef.food.domain.exception.EntidadeNaoEncontradaException;
import com.vepilef.food.domain.model.FotoProduto;
import com.vepilef.food.domain.model.Produto;
import com.vepilef.food.domain.service.CadastroProdutoService;
import com.vepilef.food.domain.service.CatalogoFotoProdutoService;
import com.vepilef.food.domain.service.FotoStorageService;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes/{idRestaurante}/produtos/{idProduto}/foto",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    @Autowired
    private CadastroProdutoService cadastroProdutoService;

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Autowired
    private FotoStorageService fotoStorageService;

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping
    public FotoProdutoModel buscar(@PathVariable Long idRestaurante, @PathVariable Long idProduto) {
        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(idRestaurante, idProduto);
        return fotoProdutoModelAssembler.toModel(fotoProduto);
    }

    @Override
    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> servirFoto
            (@PathVariable Long idRestaurante, @PathVariable Long idProduto,
             @RequestHeader(name = "accept") String acceptHeader)
            throws HttpMediaTypeNotAcceptableException {
        try {
            FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(idRestaurante, idProduto);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            FotoStorageService.FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temUrl()) {

                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {

                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas)
            throws HttpMediaTypeNotAcceptableException {

        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(
            @PathVariable Long idRestaurante, @PathVariable Long idProduto,
            @Valid FotoProdutoInput fotoProdutoInput,
            @RequestPart(required = true) MultipartFile arquivo) throws IOException {

        Produto produto = cadastroProdutoService.buscarOuFalhar(idRestaurante, idProduto);

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream());

        return fotoProdutoModelAssembler.toModel(fotoSalva);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idRestaurante, @PathVariable Long idProduto) {
        catalogoFotoProdutoService.remover(idRestaurante, idProduto);
    }

}
