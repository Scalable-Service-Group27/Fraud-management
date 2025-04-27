package com.fraud_detection.Fraud_Management.ruleengine.rules;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRule;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SuspiciousMerchantRule implements TransactionRule {

    private static final List<String> riskyAccounts = Arrays.asList("ACC6666", "ACC9999");

    @Override
    public TransactionResult apply(TransactionDTO txn) {
        if (riskyAccounts.contains(txn.getAccNoTo())) {
            return new TransactionResult(TransactionStatus.ALERT, "Transaction to suspicious merchant.");
        }
        return new TransactionResult(TransactionStatus.VALID, "Merchant clean.");
    }
}