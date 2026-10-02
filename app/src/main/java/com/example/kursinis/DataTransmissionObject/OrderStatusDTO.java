package com.example.kursinis.DataTransmissionObject;

import com.example.kursinis.model.OrderStatus;

public class OrderStatusDTO {
    private int orderId;
    private int driverId;
    private OrderStatus orderStatus;

    public OrderStatusDTO(int orderId, int driverId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.driverId = driverId;
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getDriverId() {
        return driverId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
