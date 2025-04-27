package com.fraud_detection.Fraud_Management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String accountNumber;

    @Column(name = "alert_message")
    private String alertMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
