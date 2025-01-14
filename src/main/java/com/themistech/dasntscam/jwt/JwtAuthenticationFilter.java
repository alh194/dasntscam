package com.themistech.dasntscam.jwt;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //Filtros relacionados con el token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Obtener el JWT
        final String token = getTokenFromRequest(request);
        //Si no existe el JWT...
        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }
        //Los filtros seguirán su curso
        filterChain.doFilter(request, response);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        //Recuperar encabezado
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        //Extraer el token, viene siempre tras la palabra "Bearer "
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;

    }
}
