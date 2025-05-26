package com.themistech.dasntscam.repositories;

import com.themistech.dasntscam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByCorreoElectronico(String correoElectronico);
}
