package com.themistech.dasntscam.requests;

import com.themistech.dasntscam.entities.Rol;
import com.themistech.dasntscam.entities.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private Sexo sexo;
    private String numeroDni;
    private String correoElectronico;
    private String numeroContacto;
    private LocalDate fechaNacimiento;
    private String pais;
    private String localidad;
    private String municipio;
    private String codigoPostal;
    private Rol rol = Rol.cliente;
    private String password;
}
