package com.example.kursinis.DataTransmissionObject;

import com.example.kursinis.model.OrderStatus;

import java.util.List;

public class OrderDTO {
    private int buyer;
    private int restaurant;
    private List<Integer> items;
    private double price;
    private OrderStatus ordersStatus;

    public OrderDTO(int buyer, int restaurant, double price, List<Integer> items, OrderStatus ordersStatus) {
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.price = price;
        this.items = items;
        this.ordersStatus = ordersStatus;
    }

    public int getRestaurant() {
        return restaurant;
    }

    public List<Integer> getItems() {
        return items;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getOrdersStatus() {
        return ordersStatus;
    }

    public int getBuyer() {
        return buyer;
    }
}
