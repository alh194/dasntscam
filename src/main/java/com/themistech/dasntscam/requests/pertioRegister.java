package com.themistech.dasntscam.requests;

import com.themistech.dasntscam.enums.Rol;
import com.themistech.dasntscam.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class pertioRegister {
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
    private MultipartFile cvFile;
    private MultipartFile titleFile;
}
