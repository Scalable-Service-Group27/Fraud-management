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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TestController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send-txn")
    public ResponseEntity<String> sendMockTxn() {
        TransactionDTO txn = new TransactionDTO(
                UUID.randomUUID().toString(),
                "ACC3002",
                "ACC3001",
                250000.0,
                "withdrawal",
                "2025-04-22T14:00:00"
        );

        kafkaTemplate.send(TRANSACTION_TOPIC, txn.getTransactionId(), txn);
        return ResponseEntity.ok("Sent test txn: " + txn.getTransactionId());
    }
}
