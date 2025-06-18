package com.themistech.dasntscam.repositories;

import com.themistech.dasntscam.entities.Perito;
import com.themistech.dasntscam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeritoRepository extends JpaRepository<Perito, Long> {
    Optional<Perito> findByUsuarioId(Long idUsuario);

    Perito findByUsuario(User usuario);
}
