package com.themistech.dasntscam.dto;

import com.themistech.dasntscam.entities.Cliente;
import com.themistech.dasntscam.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ClienteDTO {
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

    public static List<ClienteDTO> toClienteDTOList(List<Cliente> clientes) {
        return clientes.stream()
                .map(c -> ClienteDTO.builder()
                        .nombre(c.getUsuario().getNombre())
                        .apellido1(c.getUsuario().getApellido1())
                        .apellido2(c.getUsuario().getApellido2())
                        .sexo(c.getUsuario().getSexo())
                        .numeroDni(c.getUsuario().getNumeroDni())
                        .correoElectronico(c.getUsuario().getCorreoElectronico())
                        .numeroContacto(c.getUsuario().getNumeroContacto())
                        .fechaNacimiento(c.getUsuario().getFechaNacimiento())
                        .pais(c.getUsuario().getPais())
                        .localidad(c.getUsuario().getLocalidad())
                        .municipio(c.getUsuario().getMunicipio())
                        .codigoPostal(c.getUsuario().getCodigoPostal())
                        .libroVerde(c.getLibroVerde())
                        .polizaSeguro(c.getPolizaSeguro())
                        .build())
                .collect(Collectors.toList());
    }
}
