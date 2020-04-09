package com.example.demo.models.DTO;

public class RequestDTO {
    private int sender;
    private int receiver;

    public RequestDTO() {
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }
}
