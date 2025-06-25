package com.themistech.dasntscam.vado;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.themistech.dasntscam.auth.AuthService;
import com.themistech.dasntscam.dto.VadoAnalysis;
import com.themistech.dasntscam.entities.Cliente;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.themistech.dasntscam.repositories.ClienteRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class VadoService {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClienteRepository clienteRepository;

    //private static final Logger log = LoggerFactory.getLogger(VadoService.class);
    private final String CSV_PATH = "padron_vados.csv";

    public VadoAnalysis analyzeAndFetchData(MultipartFile file) {
        try {
            //Cliente cliente = clienteRepository.findByUsuario(user)
            //        .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            // 1) Guarda el archivo en disco
            File temp = File.createTempFile("upload-", ".jpg");
            file.transferTo(temp);

            // 2) Lanza el script Python
            //String scriptPath = "dasntscam/scripts/inference.py";
            // Determina la ruta al ejecutable de venv (Windows)
            String projectDir = System.getProperty("user.dir");  // p.ej. C:\...\dasntscam
            String pythonExe = Paths.get(projectDir, "/dasntscam/ocr", "Scripts", "python.exe")
                    .toString();
            String scriptPath = Paths.get(projectDir, "/dasntscam/scripts", "inference.py")
                    .toString();

            if (!new File(scriptPath).exists()) {
                throw new RuntimeException("No encuentro el script en " + scriptPath);
            }

            ProcessBuilder pb = new ProcessBuilder(
                    pythonExe,
                    scriptPath,
                    temp.getAbsolutePath()
            );
            Process process = pb.start();

            // 3) Lee stdout completamente
            StringBuilder stdout = new StringBuilder();
            try (BufferedReader outReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = outReader.readLine()) != null) {
                    stdout.append(line);
                }
            }

            // 4) Lee stderr (por si hay errores de Python)
            StringBuilder stderr = new StringBuilder();
            try (BufferedReader errReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = errReader.readLine()) != null) {
                    stderr.append(line).append('\n');
                }
            }

            int code = process.waitFor();
            temp.delete();

            if (code != 0) {
                throw new RuntimeException("Error en script Python: " + stderr.toString().trim());
            }

            String json = stdout.toString().trim();
            if (json.isEmpty()) {
                throw new RuntimeException("No se recibió salida del script Python");
            }

            // 6) Parseo a DTO
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, VadoAnalysis.class);

        } catch (Exception ex) {
            // Si ya era un ResponseStatusException, relánzalo
            if (ex instanceof ResponseStatusException) {
                throw (ResponseStatusException) ex;
            }
            // Envolvemos cualquier otra excepción con su mensaje
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error procesando la imagen: " + ex.getMessage(),
                    ex
            );
        }
    }

    private VadoAnalysis fetchDetailsFromCSV(String numeroPlaca) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_PATH));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.get("numeroPlaca").equalsIgnoreCase(numeroPlaca)) {
                    return new VadoAnalysis(
                            csvRecord.get("direccion"),
                            numeroPlaca,
                            csvRecord.get("vigente")
                    );
                }
            }
        }
        return new VadoAnalysis("Desconocida", numeroPlaca, "Desconocido");
        }
}
