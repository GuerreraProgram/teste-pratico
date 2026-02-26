package com.example.agenda.dto;

import java.util.List;

public class ContatoResponseDTO {

    private Long idContato;
    private String nomeContato;
    private Integer idadeContato;
    private List<TelefoneDTO> telefones;

    public Long getIdContato() {
        return idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

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

    public List<TelefoneDTO> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<TelefoneDTO> telefones) {
        this.telefones = telefones;
    }
}

