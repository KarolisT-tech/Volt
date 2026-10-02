package com.example.kursinis.DataTransmissionObject;

public class MessageDTO {
    private String message;
    private int senderId;
    private int receiverId;
    private int chatId;

    public MessageDTO(String message, int senderId, int receiverId, int chatId) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getChatId() {return chatId;}
}
