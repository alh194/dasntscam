package com.themistech.dasntscam.auth;

import com.themistech.dasntscam.requests.LoginRequest;
import com.themistech.dasntscam.requests.PartialRegisterRequest;
import com.themistech.dasntscam.requests.RegisterRequest;
import com.themistech.dasntscam.responses.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping(value = "partialRegister")
    public ResponseEntity<AuthResponse> partialRegister(@RequestBody PartialRegisterRequest request) {
        return ResponseEntity.ok(authService.partialRegister(request));
    }
}
