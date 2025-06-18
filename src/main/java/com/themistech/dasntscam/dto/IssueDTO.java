package com.themistech.dasntscam.dto;

import com.themistech.dasntscam.entities.Issue;
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

public class IssueDTO {
    String id;
    String estado;
    String nombre;
    String descripcion;
    String cliente;
    String perito;
    LocalDate fecha_creacion;

    public static List<IssueDTO> toIssueDTOList(List<Issue> issues) {
        return issues.stream()
                .map(issue -> IssueDTO.builder()
                        .id(issue.getVideoId())
                        .estado(issue.getEstado().toString())
                        .nombre(issue.getNombre())
                        .descripcion(issue.getDescripcion())
                        .cliente(issue.getCliente().getUsuario().getNombre())
                        .perito(
                                issue.getPerito() != null && issue.getPerito().getUsuario() != null
                                        ? issue.getPerito().getUsuario().getNombre()
                                        : null
                        )
                        .fecha_creacion(
                                issue.getFechaCreacion() != null
                                        ? issue.getFechaCreacion().toLocalDate()
                                        : null
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
