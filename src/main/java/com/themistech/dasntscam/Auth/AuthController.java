package com.themistech.dasntscam.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//Estos son los enpoints publicos, donde cualquiera puede realizar una peticion
public class AuthController {
    @PostMapping(value = "login")
    public String login() {
        return "Login endpoint test";
    }

    @PostMapping(value = "register")
    public String register() {
        return "Register endpoint test";
    }
}
