package com.example.bslservice.controller;

import com.example.bslservice.service.BSLParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bsl")
public class BSLController {
    
    private final BSLParserService parserService;

    @Autowired
    public BSLController(BSLParserService parserService) {
        this.parserService = parserService;
    }
    
    @PostMapping("/parse")
    public Map<String, Object> parseCode(@RequestBody String code) {
        return parserService.parseCode(code);
    }
} 