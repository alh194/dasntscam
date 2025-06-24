package com.themistech.dasntscam.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class PdfSecurityConfig {

    // 1️⃣ Cadena de seguridad exclusiva para /api/pdf/**
    @Bean
    @Order(1)  // Se evalúa antes que el resto
    public SecurityFilterChain pdfSecurityChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/pdf/**", "/api/odt/**")     // Solo rutas de informes
                .csrf(csrf -> csrf.disable())       // Sin CSRF aquí
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()       // Permite TODO sin autenticar
                );
        return http.build();
    }
}
