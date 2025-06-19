package com.themistech.dasntscam.informes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PdfReportService {

    private final SpringTemplateEngine templateEngine;

    public PdfReportService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePericialReport(ReportData data) throws IOException {
        Context ctx = new Context();
        ctx.setVariable("titulo", data.titulo());
        ctx.setVariable("codigo", data.codigo());
        ctx.setVariable("encargo", data.encargo());
        ctx.setVariable("perito", data.perito());
        ctx.setVariable("local", data.localizacion());
        ctx.setVariable("decl", data.declaraciones());
        ctx.setVariable("paginas", data.paginas());
        ctx.setVariable("cuerpo", data.cuerpo());
        ctx.setVariable("anejos", data.anejos());
        ctx.setVariable("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Procesar plantilla "informe.html" (con estilos inline en la propia plantilla)
        String html = templateEngine.process("informe", ctx);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            // Evitar problemas de CSS externo; usamos estilos embebidos
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        }
    }

    // Registros de datos del informe
    public static record ReportData(
            String titulo,
            String codigo,
            Encargo encargo,
            Perito perito,
            Localizacion localizacion,
            Declaraciones declaraciones,
            Map<String,Integer> paginas,
            Cuerpo cuerpo,
            String anejos
    ) {}

    public static record Encargo(
            String tipo,
            String organismo,
            String legales,
            String solicitante,
            String otro
    ) {}

    public static record Perito(
            String nombre,
            String titulo,
            String email,
            String telefono,
            String colegiado,
            String colegio,
            boolean visado
    ) {}

    public static record Localizacion(
            String temporal,
            String espacial
    ) {}

    public static record Declaraciones(
            String tacha,
            String abstencion,
            String recusacion,
            String veracidad
    ) {}

    public static record Cuerpo(
            String objeto,
            String alcance,
            String antecedentes,
            String preliminares,
            String limitativas,
            String documentos,
            String terminologia,
            String elementos,
            String procedimientos,
            String resultados,
            String situacion,
            String conclusiones
    ) {}
}
