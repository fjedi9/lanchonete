package com.vepilef.food.domain.service;

import com.vepilef.food.domain.exception.FotoProdutoNaoEncontradaException;
import com.vepilef.food.domain.model.FotoProduto;
import com.vepilef.food.domain.repository.ProdutoRepository;
import com.vepilef.food.domain.service.FotoStorageService.NovaFoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {

        Long restauranteId = foto.getRestauranteId();
        Long produtoId = foto.getProduto().getId();
        String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;

        Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            produtoRepository.delete(fotoExistente.get());
        }

        foto.setNomeArquivo(nomeNovoArquivo);
        foto = produtoRepository.save(foto);
        produtoRepository.flush();

        NovaFoto novaFoto = NovaFoto.builder()
                .nomeArquivo(foto.getNomeArquivo())
                .contentType(foto.getContentType())
                .size(foto.getTamanho())
                .inputStream(dadosArquivo)
                .build();

        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

        return foto;
    }

    public FotoProduto buscarOuFalhar(Long idRestaurante, Long idProduto) {
        return produtoRepository.findFotoById(idRestaurante, idProduto).orElseThrow(
                () -> new FotoProdutoNaoEncontradaException(idRestaurante, idProduto));
    }

    @Transactional
    public void remover(Long idRestaurante, Long idProduto) {
        FotoProduto fotoProduto = buscarOuFalhar(idRestaurante, idProduto);
        produtoRepository.delete(fotoProduto);
        produtoRepository.flush();

        fotoStorageService.remover(fotoProduto.getNomeArquivo());
    }
}
