package com.fraud_detection.Fraud_Management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String accountFrom;
    private String accountTo;
    private Double amount;
    private String transactionType;
    private String status; // VALID, ALERT, FRAUD
    private String reason;
    private LocalDateTime timestamp;
}