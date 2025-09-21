package com.cartola.backend.repository;

import com.cartola.backend.model.Rodada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RodadaRepository extends JpaRepository<Rodada, Long> {
    Optional<Rodada> findByNumero(Integer numero);
}