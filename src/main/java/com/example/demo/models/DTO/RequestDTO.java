package com.example.demo.models.DTO;

public class RequestDTO {
    private long sender;
    private long receiver;

    public RequestDTO() {
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }
}
