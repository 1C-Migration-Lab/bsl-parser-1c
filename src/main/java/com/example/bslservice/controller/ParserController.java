package com.example.bslservice.controller;

import com.example.bslservice.model.ParseRequest;
import com.example.bslservice.service.BSLParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/parser")
public class ParserController {

    private final BSLParserService parserService;

    @Autowired
    public ParserController(BSLParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeCode(@RequestBody ParseRequest request) {
        Map<String, Object> result = parserService.parseCode(request.getCode());
        return ResponseEntity.ok(result);
    }
} 