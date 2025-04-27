package com.fraud_detection.Fraud_Management.ruleengine.rules;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRule;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CreditLimitExceededRule implements TransactionRule {

    private static final Map<String, Double> creditLimits = new HashMap<>();

    static {
        creditLimits.put("ACC2001", 200000.0);
        creditLimits.put("ACC2002", 100000.0);
        creditLimits.put("ACC2003", 300000.0);
    }

    @Override
    public TransactionResult apply(TransactionDTO txn) {
        if (!"credit".equalsIgnoreCase(txn.getTransactionType())) {
            return new TransactionResult(TransactionStatus.VALID, "Not a credit transaction.");
        }

        Double limit = creditLimits.get(txn.getAccNoFrom());

        if (limit != null && txn.getAmount() > limit) {
            return new TransactionResult(TransactionStatus.FRAUD, "Transaction exceeds credit card limit.");
        }

        return new TransactionResult(TransactionStatus.VALID, "Credit limit within range.");
    }
}