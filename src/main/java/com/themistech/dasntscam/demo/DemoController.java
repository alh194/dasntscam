package com.themistech.dasntscam.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
//Estos son los enpoints Protegidos, donde solo los usuarios registrados puede entrar
public class DemoController {
    @PostMapping(value = "demo")
    public String welcome() {
        return "WELCOME endpoint test";
    }
}
