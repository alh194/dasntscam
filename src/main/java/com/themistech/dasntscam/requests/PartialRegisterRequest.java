package com.themistech.dasntscam.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PartialRegisterRequest {
    private String correoElectronico;
    private String password; //TODO quizás hacer encode en el front para evitar snifers
}
