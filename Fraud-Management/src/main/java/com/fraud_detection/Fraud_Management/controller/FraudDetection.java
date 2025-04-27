package com.fraud_detection.Fraud_Management.controller;

import com.fraud_detection.Fraud_Management.DTO.NotificationDTO;
import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRuleEngine;
import com.fraud_detection.Fraud_Management.service.FraudAlertedTransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import com.fraud_detection.Fraud_Management.Service.*;

import static com.fraud_detection.Fraud_Management.Kafka.KafkaTopics.NOTIFICATION_TOPIC;
@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
public class FraudDetection {

    private final TransactionRuleEngine ruleEngine;
    private final KafkaTemplate<String, NotificationDTO> notificationKafkaTemplate;
    private final com.fraud_detection.Fraud_Management.service.FraudAlertedTransactionLogService transactionLogService;
    private final FraudAlertService fraudAlertService;

    public FraudDetection(TransactionRuleEngine ruleEngine, KafkaTemplate<String, NotificationDTO> notificationKafkaTemplate, FraudAlertedTransactionLogService transactionLogService, FraudAlertService fraudAlertService) {
        this.ruleEngine = ruleEngine;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
        this.transactionLogService = transactionLogService;
        this.fraudAlertService = fraudAlertService;
    }


    // Constructor if you prefer explicit constructor


    @PostMapping("/check")
    public TransactionResult checkTransaction(@RequestBody TransactionDTO transaction) {
        // 1. Evaluate transaction
        TransactionResult result = ruleEngine.evaluateTransaction(transaction);

        // 2. Save to transaction_log table (always)
        transactionLogService.createTransactionLog(
                transaction.getTransactionId(),
                transaction.getAccNoFrom(),
                transaction.getAccNoTo(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                String.valueOf(result.getStatus()),
                result.getReason()
        );

        // 3. If FRAUD, save to fraud_alert table
        if (result.getStatus() == com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus.FRAUD) {
            fraudAlertService.createFraudAlert(
                    transaction.getTransactionId(),
                    transaction.getAccNoFrom(),
                    result.getReason()
            );
        }

        // 4. Prepare and send Notification
        NotificationDTO notification = new NotificationDTO();
        notification.setUserId(transaction.getUserId());
        notification.setAccountNo(transaction.getAccNoFrom());
        notification.setTransactionStatus(result.getStatus().toString());

        notificationKafkaTemplate.send(NOTIFICATION_TOPIC, notification);

        return result;
    }
}