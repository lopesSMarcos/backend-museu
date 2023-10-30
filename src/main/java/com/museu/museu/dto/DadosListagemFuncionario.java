package com.museu.museu.dto;

import com.museu.museu.domain.Endereco;
import com.museu.museu.domain.Funcionario;

public record DadosListagemFuncionario(Integer id, String nome, String cpf, String telefone, double salario, String rg, Endereco endereco) {

    public DadosListagemFuncionario(Funcionario f) {
        this(f.getId(), f.getNome(), f.getCpf(), f.getTelefone(), f.getSalario(), f.getRg(), f.getEndereco());
    }
}
