package com.fraud_detection.Fraud_Management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String transactionId;
    private String transactionType; // transfer, deposit, withdrawal
    private String sourceType;      // debit_card, credit_card, bank
    private Double amount;
    private String currency;
    private String timestamp;
    private String accNoFrom;
    private String accNoTo;

    public TransactionDTO(String string, String acc3002, String acc3001, double v, String withdrawal, String s) {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccNoFrom() {
        return accNoFrom;
    }

    public void setAccNoFrom(String accNoFrom) {
        this.accNoFrom = accNoFrom;
    }

    public String getAccNoTo() {
        return accNoTo;
    }

    public void setAccNoTo(String accNoTo) {
        this.accNoTo = accNoTo;
    }
}
