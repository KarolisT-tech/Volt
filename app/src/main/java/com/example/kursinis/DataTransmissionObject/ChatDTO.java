package com.example.kursinis.DataTransmissionObject;

import com.example.kursinis.model.FoodOrder;

public class ChatDTO {
    private int orderId;

    public ChatDTO(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
