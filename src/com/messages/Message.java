package com.messages;


public class Message {

    public static final int CARD = 0;

    private int type;
    private String message;

    //public Message() {}

    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return type + "|" + message;
    }
}
