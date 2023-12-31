package com.museu.museu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.museu.museu.domain.Cache;
import com.museu.museu.domain.Divisao;
import com.museu.museu.dto.DadosDivisao;
import com.museu.museu.repositories.DivisaoRepository;

import jakarta.validation.Valid;

@RequestMapping("/divisao")
@RestController
public class DivisaoController {
    
    private static final String PEGAR_DIVISAO = "divisao";
    private Cache cache = Cache.getInstance();

    private final DivisaoRepository divisaoRepository;

    @Autowired
    public DivisaoController( DivisaoRepository divisaoRepository) {
        this.divisaoRepository = divisaoRepository;
    }


    @PostMapping("/nova")
    @Transactional
    public ResponseEntity<DadosDivisao> criarDivisao(@Valid @RequestBody DadosDivisao divisao) {
        var novaDivisao = divisaoRepository.save(new Divisao(divisao));

        return ResponseEntity.ok(new DadosDivisao(novaDivisao));
    }

    @GetMapping("{id}")
    public ResponseEntity<DadosDivisao> getDivisao(@PathVariable Integer id){
        Divisao cachedDivisao = (Divisao) cache.get(PEGAR_DIVISAO + id);

        if(cachedDivisao != null) {
            return ResponseEntity.ok(new DadosDivisao(cachedDivisao));
        }

        var divisao = divisaoRepository.findById(id);

        if(divisao.isPresent()){
            cache.put(PEGAR_DIVISAO + id, divisao.get());
            return ResponseEntity.ok(new DadosDivisao(divisao.get()));
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<Page<DadosDivisao>> listarDivisao(@PageableDefault(size = 10, sort = "nome") Pageable paginacao ) {

        Page<Divisao> lista = divisaoRepository.findAll(paginacao);

        var dados = lista.getContent();

        List<DadosDivisao> dadosList = new ArrayList<>();

        for (Divisao f : dados) {
            dadosList.add(new DadosDivisao(f));
        }

        Page<DadosDivisao> dadosPage = new PageImpl<>(dadosList, paginacao, 0);

        return ResponseEntity.ok(dadosPage);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<String> excluirDivisao(@PathVariable Integer id){
        cache.remove(PEGAR_DIVISAO + id);
        divisaoRepository.deleteById(id);
        return ResponseEntity.ok("Divisão excluída com sucesso!");
    }
}
