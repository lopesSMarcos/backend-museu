package com.museu.museu.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.museu.museu.domain.CategoriaIngresso;


public interface CategoriaIngressoRepository extends JpaRepository<CategoriaIngresso, Integer> {

    Page<CategoriaIngresso> findAll(Pageable paginacao);
}
