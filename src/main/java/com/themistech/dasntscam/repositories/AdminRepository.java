package com.themistech.dasntscam.repositories;

import com.themistech.dasntscam.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsuarioId(Long idUsuario);
}
