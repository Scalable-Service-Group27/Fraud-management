package com.fraud_detection.Fraud_Management.Kafka;

import com.fraud_detection.Fraud_Management.DTO.AlertDTO;
import com.fraud_detection.Fraud_Management.DTO.NotificationDTO;
import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fraud_detection.Fraud_Management.Kafka.KafkaTopics.*;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);

    private final TransactionRuleEngine ruleEngine;
    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate; // KafkaTemplate for TransactionDTO
    private final KafkaTemplate<String, AlertDTO> alertKafkaTemplate;  // KafkaTemplate for AlertDTO
    private final KafkaTemplate<String, NotificationDTO> notificationKafkaTemplate; // KafkaTemplate for NotificationDTO

    public TransactionProducer(TransactionRuleEngine ruleEngine, KafkaTemplate<String, TransactionDTO> kafkaTemplate, KafkaTemplate<String, AlertDTO> alertKafkaTemplate, KafkaTemplate<String, NotificationDTO> notificationKafkaTemplate) {
        this.ruleEngine = ruleEngine;
        this.kafkaTemplate = kafkaTemplate;
        this.alertKafkaTemplate = alertKafkaTemplate;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    /**
     * Processes a transaction, evaluates it, and produces the appropriate Kafka messages.
     *
     * @param transaction the transaction to process
     */
    public void processTransaction(TransactionDTO transaction) {
        if (transaction.getTransactionType() == null) {
            logger.warn("Received transaction with null transactionType: {}", transaction);
            return;  // Skip processing this invalid transaction
        }

        logger.info("Processing transaction: {}", transaction);

        // Evaluate the transaction
        TransactionResult result = ruleEngine.evaluateTransaction(transaction);

        // Always send to notification topic
        sendNotification(transaction, result);

        switch (result.getStatus()) {
            case VALID:
                // Send to valid transaction topic
                kafkaTemplate.send(VALID_TXN_TOPIC, transaction.getTransactionId(), transaction);
                logger.info("Transaction valid: {}", transaction.getTransactionId());
                break;

            case FRAUD:
                // Send to rollback topic for fraud transaction
                kafkaTemplate.send(ROLLBACK_TXN_TOPIC, transaction.getTransactionId(), transaction);
                // Send fraud alert
                System.out.println("FRAUD detected66");

                AlertDTO fraudAlert = new AlertDTO(
                        transaction.getTransactionId(),
                        result.getReason(),
                        transaction.getAccNoFrom()
                );
                alertKafkaTemplate.send(ALERT_TOPIC, fraudAlert);
                logger.info("Fraud detected! Transaction rolled back and alert sent.");
                System.out.println("FRAUD detected44");
                break;

            case ALERT:
                // Send to valid transaction topic for alert-worthy transaction
                kafkaTemplate.send(VALID_TXN_TOPIC, transaction.getTransactionId(), transaction);
                System.out.println("ALERT detected11");

                // Send alert
                AlertDTO alert = new AlertDTO(
                        transaction.getTransactionId(),
                        result.getReason(),
                        transaction.getAccNoFrom()
                );
                alertKafkaTemplate.send(ALERT_TOPIC, alert);
                logger.info("Alert-worthy transaction. Proceeded but alert sent.");
                System.out.println("ALERT detected22");

                break;

            default:
                logger.error("Unknown transaction status.");
                System.out.println("UNKNOWN");

        }
    }

    /**
     * Sends notification to the notification topic.
     *
     * @param transaction the transaction being processed
     * @param result      the result of the transaction evaluation
     */
    private void sendNotification(TransactionDTO transaction, TransactionResult result) {
        // Create a notification DTO
        NotificationDTO notification = new NotificationDTO(
                transaction.getUserId(),
                transaction.getAccNoFrom(),
                result.getStatus().toString()
        );

        // Send to notification topic
        notificationKafkaTemplate.send(NOTIFICATION_TOPIC, notification);
        logger.info("Notification sent for transaction: {}", transaction.getTransactionId());
    }
}