package com.themistech.dasntscam.responses;

import java.util.List;
import com.themistech.dasntscam.dto.ClienteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ClienteListResponse {
    private List<ClienteDTO> clienteList;
}
