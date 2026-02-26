package com.example.agenda.repository;

import com.example.agenda.entity.Contato;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    @Query(value = "SELECT DISTINCT c.id_contato FROM contato c " +
                   "LEFT JOIN telefone t ON c.id_contato = t.id_contato " +
                   "WHERE (CAST(:nome AS TEXT) IS NULL OR :nome = '' OR LOWER(c.nome_contato) LIKE LOWER('%' || CAST(:nome AS TEXT) || '%')) " +
                   "AND (CAST(:telefone AS TEXT) IS NULL OR :telefone = '' OR t.numero_telefone LIKE '%' || CAST(:telefone AS TEXT) || '%')",
            nativeQuery = true)
    List<Long> pesquisarIds(@Param("nome") String nome, @Param("telefone") String telefone);

    @EntityGraph(attributePaths = "telefones")
    @Query("SELECT c FROM Contato c WHERE c.idContato IN :ids")
    List<Contato> findByIdContatoInWithTelefones(@Param("ids") List<Long> ids);
}

