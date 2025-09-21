package com.cartola.backend.repository;

import com.cartola.backend.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
    Optional<Time> findByNomeUsuario(String nomeUsuario);
}