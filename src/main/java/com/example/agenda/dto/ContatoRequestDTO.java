package com.example.agenda.dto;

import java.util.List;

public class ContatoRequestDTO {

    private String nomeContato;
    private Integer idadeContato;
    private List<String> telefones;

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public Integer getIdadeContato() {
        return idadeContato;
    }

    public void setIdadeContato(Integer idadeContato) {
        this.idadeContato = idadeContato;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }
}

