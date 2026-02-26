package com.example.agenda.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato")
    private Long idContato;

    @Column(name = "nome_contato", nullable = false, length = 200)
    private String nomeContato;

    @Column(name = "idade_contato")
    private Integer idadeContato;

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Telefone> telefones = new ArrayList<>();

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

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }
}

