package com.fraud_detection.Fraud_Management.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/f")
public class FraudCheckController {

    // Simple test endpoint to check if the controller works without Kafka
    @GetMapping("/c")
    public String checkFraud() {
        return "Fraud check passed without Kafka!";
    }

}
