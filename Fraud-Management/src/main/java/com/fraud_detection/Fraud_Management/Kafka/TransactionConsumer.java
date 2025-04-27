package com.fraud_detection.Fraud_Management.Kafka;

import com.fraud_detection.Fraud_Management.DTO.AlertDTO;
import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.fraud_detection.Fraud_Management.Kafka.KafkaTopics.*;

@Service
@EnableKafka
public class TransactionConsumer {

    private final TransactionRuleEngine ruleEngine;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TransactionConsumer(TransactionRuleEngine ruleEngine, KafkaTemplate<String, Object> kafkaTemplate) {
        this.ruleEngine = ruleEngine;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = TRANSACTION_TOPIC, groupId = "fraud-group", containerFactory = "transactionKafkaListenerFactory")
    public void listen(TransactionDTO transaction) {
        System.out.println("🚨 Received transaction: " + transaction);

        TransactionResult result = ruleEngine.evaluateTransaction(transaction);

        switch (result.getStatus()) {
            case VALID:
                kafkaTemplate.send(VALID_TXN_TOPIC, transaction.getTransactionId(), transaction);
                System.out.println("✅ Transaction valid: " + transaction.getTransactionId());
                break;

            case FRAUD:
                kafkaTemplate.send(ROLLBACK_TXN_TOPIC, transaction.getTransactionId(), transaction);
                AlertDTO fraudAlert = new AlertDTO(
                        transaction.getTransactionId(),
                        result.getReason(),
                        transaction.getAccNoFrom()
                );
                kafkaTemplate.send(ALERT_TOPIC, fraudAlert);
                System.out.println("❌ Fraud detected! Transaction rolled back and alert sent.");
                break;

            case ALERT:
                kafkaTemplate.send(VALID_TXN_TOPIC, transaction.getTransactionId(), transaction);
                AlertDTO alert = new AlertDTO(
                        transaction.getTransactionId(),
                        result.getReason(),
                        transaction.getAccNoFrom()
                );
                kafkaTemplate.send(ALERT_TOPIC, alert);
                System.out.println("⚠️ Alert-worthy transaction. Proceeded but alert sent.");
                break;

            default:
                System.err.println("Unknown transaction status.");
        }
    }
}