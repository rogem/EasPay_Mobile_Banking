package com.example.prototype10;

public class HistoryModel {
    String sender,receiver, amount,message;

    public HistoryModel(){
    }

    public HistoryModel(String sender, String receiver, String amount, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }
}
