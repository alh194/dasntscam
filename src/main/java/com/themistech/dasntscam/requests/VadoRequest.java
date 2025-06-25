package com.themistech.dasntscam.requests;

import com.themistech.dasntscam.dto.VadoAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class VadoRequest {
    String videoId;
    MultipartFile file;
}
