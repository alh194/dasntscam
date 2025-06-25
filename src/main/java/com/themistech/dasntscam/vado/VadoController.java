package com.themistech.dasntscam.vado;

import com.amazonaws.services.iotevents.model.AnalysisResult;
import com.themistech.dasntscam.auth.AuthService;
import com.themistech.dasntscam.dto.VadoAnalysis;
import com.themistech.dasntscam.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vado")

public class VadoController {

    @Autowired
    private AuthService authService;
    @Autowired
    private VadoService vadoService;

    private static final Logger log = LoggerFactory.getLogger(VadoController.class);


    @PostMapping(
            value    = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<VadoAnalysis> uploadVadoAnalysis(
            //@RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
            //@RequestParam("vadoId") String vadoId,
            //@RequestParam("file") MultipartFile file) {

        //VadoAnalysis result = vadoService.analyzeAndFetchData(file);
        //return ResponseEntity.ok(result);
        if (file == null || file.isEmpty()) {
            log.warn("Llegó petición sin fichero o fichero vacío");
            return ResponseEntity
                    .badRequest()
                    .body(new VadoAnalysis("","", "Error: no file"));
        }

        // 2) Loguea sus propiedades básicas
        log.info("Recibido fichero: name='{}', originalName='{}', size={} bytes, contentType='{}'",
                file.getName(),              // aquí será "file"
                file.getOriginalFilename(),  // p.ej. "vado.jpg"
                file.getSize(),              // tamaño en bytes
                file.getContentType());      // "image/jpeg", etc.

        VadoAnalysis result = vadoService.analyzeAndFetchData(file);
        return ResponseEntity.ok(result);
    }
}
