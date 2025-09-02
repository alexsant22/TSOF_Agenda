package com.example.AgendaBack.repository;

import com.example.AgendaBack.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {

    // Verifica se email existe
    boolean existsByEmail(String email);

    // Verifica se telefone existe
    boolean existsByTelefone(String telefone);

    // Verifica se email existe excluindo um ID
    @Query("SELECT COUNT(c) > 0 FROM Contato c WHERE c.email = :email AND c.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    // Verifica se telefone existe excluindo um ID
    @Query("SELECT COUNT(c) > 0 FROM Contato c WHERE c.telefone = :telefone AND c.id != :id")
    boolean existsByTelefoneAndIdNot(@Param("telefone") String telefone, @Param("id") Long id);

    // Busca por nome (case insensitive)
    List<Contato> findByNomeContainingIgnoreCase(String nome);
}
