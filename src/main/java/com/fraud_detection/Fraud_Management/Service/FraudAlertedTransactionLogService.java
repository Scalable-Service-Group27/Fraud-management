package com.fraud_detection.Fraud_Management.service;

import com.fraud_detection.Fraud_Management.entity.FraudAlertedTransactionLog;
import com.fraud_detection.Fraud_Management.repository.FraudAlertedTransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FraudAlertedTransactionLogService {

    private final FraudAlertedTransactionLogRepository transactionLogRepository;

    public FraudAlertedTransactionLogService(FraudAlertedTransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    public FraudAlertedTransactionLog saveTransactionLog(String transactionId,
                                                         String accountFrom,
                                                         String accountTo,
                                                         Double amount,
                                                         String transactionType,
                                                         String status,
                                                         String reason) {
        FraudAlertedTransactionLog log = new FraudAlertedTransactionLog();
        log.setTransactionId(transactionId);
        log.setAccountFrom(accountFrom);
        log.setAccountTo(accountTo);
        log.setAmount(amount);
        log.setTransactionType(transactionType);
        log.setStatus(status);
        log.setReason(reason);
        log.setTimestamp(LocalDateTime.now());

        return transactionLogRepository.save(log);
    }


    public void createTransactionLog(String transactionId, String accNoFrom, String accNoTo,
                                     Double amount, String transactionType,
                                     String status, String reason) {

        // Create the Transaction Log entry
        FraudAlertedTransactionLog transactionLog = new FraudAlertedTransactionLog();

        transactionLog.setTransactionId(transactionId);
        transactionLog.setAccountFrom(accNoFrom);
        transactionLog.setAccountTo(accNoTo);
        transactionLog.setAmount(amount);
        transactionLog.setTransactionType(transactionType);
        transactionLog.setStatus(status); // VALID, ALERT, FRAUD
        transactionLog.setReason(reason);
        transactionLog.setTimestamp(LocalDateTime.now()); // Set current timestamp

        // Save it to the database
        transactionLogRepository.save(transactionLog);
    }
}