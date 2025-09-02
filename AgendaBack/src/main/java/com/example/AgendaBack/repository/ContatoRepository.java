package com.example.AgendaBack.repository;

import com.example.AgendaBack.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
}
