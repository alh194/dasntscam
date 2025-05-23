package com.themistech.dasntscam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellido_1", length = 50)
    private String apellido1;

    @Column(name = "apellido_2", length = 50)
    private String apellido2;

    @Column(name = "sexo", columnDefinition = "ENUM('M', 'F')")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(name = "numero_dni", length = 20, unique = true)
    private String numeroDni;

    @Column(name = "correo_electronico", length = 100, unique = true)
    private String correoElectronico;

    @Column(name = "numero_contacto", length = 20)
    private String numeroContacto;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "pais", length = 50)
    private String pais;

    @Column(name = "localidad", length = 100)
    private String localidad;

    @Column(name = "municipio", length = 100)
    private String municipio;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", columnDefinition = "ENUM('cliente', 'perito', 'admin')", nullable = false)
    private Rol rol = Rol.cliente;

    @Column(name = "contrasena_hash", length = 60, nullable = false)
    private String contrasenaHash;

    @Column(name = "fecha_creacion", updatable = false, insertable = false)
    private java.sql.Timestamp fechaCreacion;

    //Utilidades
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasenaHash;
    }

    @Override
    public String getUsername() {
        return nombre;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
