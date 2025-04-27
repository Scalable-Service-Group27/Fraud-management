package com.fraud_detection.Fraud_Management.ruleengine.rules;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRule;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GeoLocationMismatchRule implements TransactionRule {

    private static final Map<String, String> accountLocation = new HashMap<>();

    static {
        accountLocation.put("ACC3001", "Delhi");
        accountLocation.put("ACC3002", "Mumbai");
    }

    @Override
    public TransactionResult apply(TransactionDTO txn) {
        String expectedLocation = accountLocation.get(txn.getAccNoFrom());
        String actualLocation = "Bangalore"; // Mocked for now

        if (expectedLocation != null && !expectedLocation.equals(actualLocation)) {
            return new TransactionResult(TransactionStatus.ALERT, "Geolocation mismatch for account.");
        }
        return new TransactionResult(TransactionStatus.VALID, "Location matched.");
    }
}