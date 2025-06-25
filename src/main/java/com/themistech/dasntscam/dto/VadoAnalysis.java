package com.themistech.dasntscam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VadoAnalysis {
    private String direccion;
    private String placa;
    private String vigente;
}
