package com.themistech.dasntscam.informes;

import com.amazonaws.transform.MapEntry;
import com.themistech.dasntscam.auth.AuthService;
import com.themistech.dasntscam.entities.User;
import com.themistech.dasntscam.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;
import org.odftoolkit.simple.text.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.odftoolkit.simple.TextDocument;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(FileController.class);


    @GetMapping("/static")
    public ResponseEntity<InputStreamResource> download() throws Exception {

        // ❶ Localizamos el recurso dentro del classpath
        ClassPathResource pdf = new ClassPathResource("com/themistech/dasntscam/informes/template_odt.odt");

        // ❷ Lo exponemos como InputStreamResource para hacer streaming
        InputStreamResource resource = new InputStreamResource(pdf.getInputStream());

        return ResponseEntity.ok()
                .contentLength(pdf.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.oasis.opendocument.text"))              // o APPLICATION_OCTET_STREAM
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"template_odt.odt\"")
                .body(resource);
    }

    @GetMapping("/odt")
    public ResponseEntity<InputStreamResource> generateOdt(
            @RequestParam(defaultValue = "Anónimo") String nombre,
            @RequestParam(defaultValue = "—") String curso) throws Exception {

        /* ❶ Crear documento vacío */
        TextDocument doc = TextDocument.newTextDocument();

        /* ❷ Escribir contenido dinámico */
        doc.addParagraph("Certificado de participación");
        doc.addParagraph("Se hace constar que:");
        doc.addParagraph(nombre);
        doc.addParagraph("ha completado satisfactoriamente el curso:");
        doc.addParagraph("Fecha: " + java.time.LocalDate.now());

        /* ❸ Guardar a fichero temporal */
        File temp = File.createTempFile("certificado-", ".odt");
        doc.save(temp);

        /* ❹ Devolver como descarga */
        InputStreamResource resource = new InputStreamResource(new FileInputStream(temp));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.oasis.opendocument.text"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"certificado.odt\"")
                .contentLength(temp.length())
                .body(resource);
    }
    @Autowired
    AuthService authService;
    @GetMapping("/generate")
    public ResponseEntity<InputStreamResource> generateOdt(@RequestHeader("Authorization") String header) throws Exception {
        String token = header.substring(7);          // "Bearer …"
        User user = authService.getUserWithToken(token);

        TextDocument doc = TextDocument.newTextDocument();
        doc.addParagraph("Informe de usuario autenticado");
        doc.addParagraph("Nombre: " + user.getNombre());
        doc.addParagraph("Email: " + user.getCorreoElectronico());
        doc.addParagraph("Fecha: " + java.time.LocalDate.now());

        File temp = File.createTempFile("informe-", ".odt");
        doc.save(temp);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(temp));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.oasis.opendocument.text"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"informe.odt\"")
                .contentLength(temp.length())
                .body(resource);
    }


    @GetMapping("/template")
    public ResponseEntity<InputStreamResource> generateTemplate(
            @RequestHeader("Authorization") String header) throws Exception {


        try{
            /* 1) Usuario autenticado a partir del token ──────────────────────────── */
            String token = header.substring(7);           //  "Bearer …"
            User perito = authService.getUserWithToken(token);


            // 2) Cargar la plantilla ODT desde resources/templates/
            ClassPathResource tpl = new ClassPathResource("templates/template_odt.odt");
            if (!tpl.exists()) {
                throw new FileNotFoundException("Plantilla ODT no encontrada");
            }
            log.info("✅ Plantilla encontrada en: {}", tpl.getURL());
            TextDocument doc = TextDocument.loadDocument(tpl.getInputStream());

            if (!tpl.exists()) {
                throw new FileNotFoundException("Plantilla ODT no encontrada");
            } else {
                // Opcional: muestra la ruta absoluta desde la que se carga
                log.info("✅  Plantilla encontrada en: {}", tpl.getURL());  // p. ej. jar:file:/.../target/classes!/templates/template_odt.odt
            }

            /* 3) Variables a reemplazar ─────────────────────────────────────────── */
            Map<String, String> vars = Map.ofEntries(
                    Map.entry("nombre_perito",        perito.getNombre()),
                    Map.entry("apellido1_perito",     perito.getApellido1()),
                    Map.entry("apellido2_perito",     perito.getApellido2()),
                    Map.entry("dni_perito",           perito.getNumeroDni()),
                    //Map.entry("direccion_perito",     perito.getDireccion()),
                    Map.entry("localidad_perito",     perito.getLocalidad()),
                    Map.entry("municipio_perito",     perito.getMunicipio()),
                    Map.entry("codigo_postal_perito", perito.getCodigoPostal()),
                    Map.entry("telefono_perito",      perito.getNumeroContacto()),
                    Map.entry("fecha_emision",        LocalDate.now().toString()),
                    //Map.entry("colegio_profesional",  perito.getColegioPrefesional()),
                    //Map.entry("numero_colegiado",     perito.getNumeroColegiado()),
                    // ← Texto estático
                    Map.entry("promesa",            "Promesa"),
                    Map.entry("titulo1",            "Objetivos"),
                    Map.entry("titulo2",            "Antecedentes"),
                    Map.entry("titulo3",            "Consideraciones"),
                    Map.entry("titulo3_1",            "Consideraciones preliminares"),
                    Map.entry("titulo3_2",            "Consideraciones limitativas"),
                    Map.entry("titulo4",                "Documentos de referencia"),
                    Map.entry("titulo5",            "Terminología y abreviaturas"),
                    Map.entry("titulo6",            "Desarrollo del análisis"),
                    Map.entry("titulo7",            "Conclusiones"),
                    Map.entry("anejo1",            "Curriculum Vitae")
            );

            /* 4) Sustitución de marcadores ${…} en la primera página ─────────────── */
            for (var entry : vars.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                TextNavigation nav = new TextNavigation(Pattern.quote(placeholder), doc);
                while (nav.hasNext()) {
                    TextSelection sel = (TextSelection) nav.nextSelection();
                    sel.replaceWith(entry.getValue() == null ? "" : entry.getValue());
                }
            };

            // 5. Buscar marcador de anexo: ${anexo1}
            TextNavigation navAnexo = new TextNavigation(Pattern.quote("${anejo1}"), doc);
            if (navAnexo.hasNext()) {
                TextSelection selAnexo = (TextSelection) navAnexo.nextSelection();
                selAnexo.replaceWith(""); // Elimina el marcador

                // 6. Cargar contenido del anexo
                ClassPathResource anexoRes = new ClassPathResource("templates/curriculum.odt");
                TextDocument anexo = TextDocument.loadDocument(anexoRes.getInputStream());

                // 7. Anexar cada párrafo
                anexo.getParagraphIterator().forEachRemaining(p -> {
                    Paragraph parrafoNuevo = doc.addParagraph(p.getTextContent());
                    // Si quieres copiar estilos, hazlo aquí manualmente
                });
            }


            /* 5) Guardar en archivo temporal y enviarlo ─────────────────────────── */
            File temp = File.createTempFile("informe-perito-", ".odt");
            doc.save(temp);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.oasis.opendocument.text"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"informe-perito.odt\"")
                    .contentLength(temp.length())
                    .body(new InputStreamResource(new FileInputStream(temp)));
        } catch (Exception ex) {
            ex.printStackTrace();  // correcto
            throw ex;
        }

    }
}
