package com.themistech.dasntscam.repositories;

import com.themistech.dasntscam.entities.Cliente;
import com.themistech.dasntscam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuarioId(Long idUsuario);

    Optional<Cliente> findByUsuario(User usuario);
}
