package com.example.agenda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "telefone")
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefone")
    private Long idTelefone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contato", nullable = false)
    private Contato contato;

    @Column(name = "numero_telefone", nullable = false, length = 20)
    private String numeroTelefone;

    public Long getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(Long idTelefone) {
        this.idTelefone = idTelefone;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
}

