package com.fraud_detection.Fraud_Management.controller;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.fraud_detection.Fraud_Management.Kafka.KafkaTopics.TRANSACTION_TOPIC;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate; // Specify the type as TransactionDTO

    // Constructor injection (spring will automatically inject the KafkaTemplate)
    public TestController(KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send-txn")
    public ResponseEntity<String> sendMockTxn() {
        // Create a mock transaction with all required fields
        TransactionDTO txn = new TransactionDTO(
                UUID.randomUUID().toString(),       // transactionId
                "withdrawal",                       // transactionType
                "ACC3002",                          // accNoFrom
                "ACC3001",                          // accNoTo
                250000.0,                           // amount
                "USD",                              // currency
                "2025-04-22T14:00:00",              // timestamp
                "credit_card"  ,
                "him11"// sourceType
        );

        // Send the transaction to Kafka
        kafkaTemplate.send(TRANSACTION_TOPIC, txn.getTransactionId(), txn);
        return ResponseEntity.ok("Sent test txn: " + txn.getTransactionId());
    }
}