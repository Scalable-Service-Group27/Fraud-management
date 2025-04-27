package com.fraud_detection.Fraud_Management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;
}