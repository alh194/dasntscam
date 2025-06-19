package com.themistech.dasntscam.informes;

import java.io.IOException;
import java.util.Map;
import static java.util.Map.entry;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf/report")
public class PdfReportController {

    private final PdfReportService pdfService;

    public PdfReportController(PdfReportService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ByteArrayResource> getReport(@PathVariable String reportId) throws IOException {
        // Cargar datos del informe por ID
        PdfReportService.ReportData data = fetchReportData(reportId);

        // Generar PDF
        byte[] pdf = pdfService.generatePericialReport(data);
        ByteArrayResource resource = new ByteArrayResource(pdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"informe_" + reportId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(resource);
    }

    private PdfReportService.ReportData fetchReportData(String id) {
        // Ejemplo de datos dummy; reemplaza por tu lógica real
        Map<String, Integer> paginas = Map.ofEntries(
                entry("objeto",        2),
                entry("alcance",       3),
                entry("antecedentes",  4),
                entry("preliminares",  5),
                entry("limitativas",   6),
                entry("documentos",    7),
                entry("terminologia",  8),
                entry("elementos",     9),
                entry("procedimientos",10),
                entry("resultados",   11),
                entry("situacion",    12),
                entry("conclusiones", 13),
                entry("anejos",       14)
        );

        return new PdfReportService.ReportData(
                "Informe Pericial de Ejemplo",          // titulo
                "250620DSCM00042",                     // codigo
                new PdfReportService.Encargo(
                        "judicial", "Juzgado nº1", "D. X, Letrado", null, null
                ),
                new PdfReportService.Perito(
                        "D. Perito", "Ingeniero Forense", "perito@ejemplo.com", "600123456",
                        "12345", "Colegio Profesional XYZ", true
                ),
                new PdfReportService.Localizacion(
                        "20/06/2025", "Málaga, Calle Falsa 123"
                ),
                new PdfReportService.Declaraciones(
                        null, null, null, "El perito jura veracidad"
                ),
                paginas,
                new PdfReportService.Cuerpo(
                        "Descripción del objeto del informe...",
                        "Descripción del alcance...",
                        "Relato de antecedentes...",
                        "Consideraciones preliminares...",
                        "Limitaciones y restricciones...",
                        "Lista de normas y documentos de referencia...",
                        "Términos y abreviaturas...",
                        "Elementos objeto del estudio...",
                        "Procedimientos y métodos utilizados...",
                        "Resultados obtenidos y análisis...",
                        "Situación y conservación de evidencias...",
                        "Conclusiones técnicas del perito..."
                ),
                "Anejo 1: Fotografías; Anejo 2: Informes técnicos..."
        );
    }
}
