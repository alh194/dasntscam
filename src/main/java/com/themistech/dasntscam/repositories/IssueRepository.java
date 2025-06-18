package com.themistech.dasntscam.repositories;

import com.themistech.dasntscam.entities.Issue;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("""
    SELECT i FROM Issue i
    JOIN FETCH i.cliente c
    JOIN FETCH c.usuario cu
    LEFT JOIN FETCH i.perito p
    LEFT JOIN FETCH p.usuario pu
    WHERE c.usuario = :user OR p.usuario = :user
""")
    List<Issue> findAllByUsuario(@Param("user") User user);

    List<Issue> findByEstado(IssueStatus estado);
}
