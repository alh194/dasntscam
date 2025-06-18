package com.themistech.dasntscam.auth;

import com.themistech.dasntscam.requests.LoginRequest;
import com.themistech.dasntscam.requests.clienteRegister;
import com.themistech.dasntscam.requests.pertioRegister;
import com.themistech.dasntscam.responses.AuthResponse;
import com.themistech.dasntscam.responses.RoleResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//Estos son los enpoints publicos, donde cualquiera puede realizar una peticion
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    //Registro de perito
    @PostMapping(value = "pertioRegister", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> pertioRegister(@ModelAttribute pertioRegister request) {
        return ResponseEntity.ok(authService.pertioRegister(request));
    }
    //Registro de cliente
    @PostMapping(value = "clienteRegister")
    public ResponseEntity<AuthResponse> clienteRegister(@RequestBody clienteRegister request) {
        return ResponseEntity.ok(authService.clienteRegister(request));
    }
    //Mirar el rol con el token
    @GetMapping(value = "getUserRoleWithToken")
    public ResponseEntity<RoleResponse> checkUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.getUserRoleWithToken(token));
    }
}
