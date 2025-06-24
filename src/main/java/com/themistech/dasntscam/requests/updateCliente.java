package com.themistech.dasntscam.requests;

import com.themistech.dasntscam.enums.Sexo;

import java.time.LocalDate;

public class updateCliente {
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
    private byte[] libroVerde;
    private byte[] polizaSeguro;
}
