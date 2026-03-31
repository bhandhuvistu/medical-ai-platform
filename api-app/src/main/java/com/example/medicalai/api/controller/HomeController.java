package com.example.medicalai.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Medical AI Platform");
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "0.1.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Medical AI API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Medical AI Platform");
        response.put("version", "0.1.0");
        response.put("description", "AI-powered medical diagnostic platform");
        response.put("endpoints", new String[]{
            "/",
            "/api/health",
            "/api/info",
            "/actuator/health",
            "/swagger-ui.html"
        });
        return ResponseEntity.ok(response);
    }
}