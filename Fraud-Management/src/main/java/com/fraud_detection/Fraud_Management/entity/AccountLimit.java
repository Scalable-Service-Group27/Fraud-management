package com.fraud_detection.Fraud_Management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "daily_limit", nullable = false)
    private Double dailyLimit;

    @Column(name = "transaction_limit", nullable = false)
    private Double transactionLimit;
}