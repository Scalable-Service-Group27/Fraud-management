package com.fraud_detection.Fraud_Management.Kafka;

public class KafkaTopics {
    public static final String TRANSACTION_TOPIC = "transaction-events";
    public static final String VALID_TXN_TOPIC = "valid-transactions";
    public static final String ROLLBACK_TXN_TOPIC = "rollback-transactions";
    public static final String ALERT_TOPIC = "fraud-alerts";
}
