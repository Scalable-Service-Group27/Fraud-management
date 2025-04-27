package com.fraud_detection.Fraud_Management.ruleengine.rules;

import com.fraud_detection.Fraud_Management.DTO.TransactionDTO;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionResult;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionRule;
import com.fraud_detection.Fraud_Management.ruleengine.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class BlacklistedAccountRule implements TransactionRule {

    private static final List<String> BLACKLISTED_ACCOUNTS = Arrays.asList("ACC1002", "ACC9876", "ACC4444");

    @Override
    public TransactionResult apply(TransactionDTO txn) {
        if (BLACKLISTED_ACCOUNTS.contains(txn.getAccNoFrom()) || BLACKLISTED_ACCOUNTS.contains(txn.getAccNoTo())) {
            return new TransactionResult(TransactionStatus.FRAUD, "Blacklisted account involved in transaction.");
        }
        return new TransactionResult(TransactionStatus.VALID, "Accounts are clean.");
    }
}