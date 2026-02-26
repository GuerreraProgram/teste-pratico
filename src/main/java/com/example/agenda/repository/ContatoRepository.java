package com.example.agenda.repository;

import com.example.agenda.entity.Contato;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @EntityGraph(attributePaths = "telefones")
    @Query("select distinct c from Contato c left join c.telefones t " +
           "where (:nome is null or lower(c.nomeContato) like lower(concat('%', :nome, '%'))) " +
           "and (:telefone is null or t.numeroTelefone like concat('%', :telefone, '%'))")
    List<Contato> pesquisar(@Param("nome") String nome, @Param("telefone") String telefone);
}

