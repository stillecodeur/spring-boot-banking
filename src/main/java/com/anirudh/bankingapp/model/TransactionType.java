package com.anirudh.bankingapp.model;

public enum TransactionType {
    DEPOSIT("deposit"), WITHDRAW("withdraw"), TRANSFER("transfer");

    private final String transactionType;

    TransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }


}
