package com.themistech.dasntscam.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

//TODO considerar borrarlo
public class TokenRequest {
    String token;
}
