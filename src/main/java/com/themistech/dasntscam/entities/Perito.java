package com.themistech.dasntscam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "peritos")
public class Perito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_peritos_usuario"))
    private User usuario;

    @Column(name = "colegio_profesional", length = 100, nullable = false)
    private String colegioProfesional;

    @Column(name = "numero_colegiado", length = 30, nullable = false)
    private String numeroColegiado;

    @Column(name = "codigo_registro", length = 30, nullable = false)
    private String codigoRegistro;

    @Lob
    @Column(name = "cv", columnDefinition = "LONGBLOB")
    private byte[] cv;
}
