package com.example.project1.controller;

import com.example.project1.entity.dto.NLPResponse;
import com.example.project1.service.PricePredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PricePredictionController {

    private final PricePredictionService pricePredictionService;

    @PostMapping("/predict")
    public ResponseEntity<String> technicalAnalysis(@RequestParam(name = "companyId") Long companyId) {
        String response = pricePredictionService.technicalAnalysis(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analyze")
    public ResponseEntity<NLPResponse> nlp(@RequestParam(name = "companyId") Long companyId) throws Exception {
        NLPResponse response = pricePredictionService.nlp(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/predict-next-month-price")
    public ResponseEntity<Double> lstm(@RequestParam(name = "companyId") Long companyId) {
        Double response = pricePredictionService.lstm(companyId);
        return ResponseEntity.ok(response);
    }
}
