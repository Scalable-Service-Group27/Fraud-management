package com.fraud_detection.Fraud_Management.ruleengine.rules;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRule;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InsufficientBalanceRule implements TransactionRule {

    private static final Map<String, Double> accountBalances = new HashMap<>();

    static {
        accountBalances.put("ACC3001", 50000.0);
        accountBalances.put("ACC3002", 200000.0);
    }

    @Override
    public TransactionResult apply(TransactionDTO txn) {
        if (!txn.getTransactionType().equalsIgnoreCase("withdrawal") && !txn.getTransactionType().equalsIgnoreCase("transfer")) {
            return new TransactionResult(TransactionStatus.VALID, "Not applicable for deposits.");
        }

        Double balance = accountBalances.getOrDefault(txn.getAccNoFrom(), 0.0);
        if (txn.getAmount() > balance) {
            return new TransactionResult(TransactionStatus.FRAUD, "Insufficient account balance.");
        }
        return new TransactionResult(TransactionStatus.VALID, "Balance sufficient.");
    }
}