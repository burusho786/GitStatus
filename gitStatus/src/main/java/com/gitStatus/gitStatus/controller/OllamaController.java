package com.gitStatus.gitStatus.controller;


import com.gitStatus.gitStatus.ollamaservices.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ollama")
@RequiredArgsConstructor
public class OllamaController {

    private final OllamaService ollamaService;

    @GetMapping("/test")
    public String test() {

        return ollamaService.askModel(

                "Explain Spring Boot in one sentence."

        );
    }
}