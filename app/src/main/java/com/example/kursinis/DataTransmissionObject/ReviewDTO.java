package com.example.kursinis.DataTransmissionObject;

public class ReviewDTO {

    private int rating;
    private String message;
    private int senderId;
    private int receiverId;
    private int orderId;

    public ReviewDTO(int rating, String message, int senderId, int receiverId, int orderId) {
        this.rating = rating;
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.orderId = orderId;
    }

    public int getRating() {
        return rating;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }
}
