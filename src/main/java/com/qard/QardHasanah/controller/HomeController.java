package com.qard.QardHasanah.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Qard Hasanah API! Visit /swagger-ui.html for API documentation";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is running!";
    }
}

