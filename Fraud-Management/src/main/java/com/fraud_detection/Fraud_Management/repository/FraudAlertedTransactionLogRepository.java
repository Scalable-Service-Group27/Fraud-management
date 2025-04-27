package com.fraud_detection.Fraud_Management.repository;

import com.fraud_detection.Fraud_Management.entity.FraudAlertedTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudAlertedTransactionLogRepository extends JpaRepository<FraudAlertedTransactionLog, Long> {
    // You can add custom queries later if needed
}