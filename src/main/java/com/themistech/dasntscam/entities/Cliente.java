package com.themistech.dasntscam.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_clientes_usuario"))
    private User usuario;

    @Column(name = "libro_verde", length = 100, nullable = false)
    private String libroVerde;

    @Column(name = "poliza_seguro", length = 30, nullable = false)
    private String polizaSeguro;
}
