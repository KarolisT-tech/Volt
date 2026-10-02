package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrder {
    private int id;
    private String name;
    private double price;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;


    private BasicUser buyer;
    private List<Cuisine> items;
    private Chat chat;
    private Restaurant restaurant;
    private OrderStatus orderStatus;
    private Driver driver;
    private Review review;

    public FoodOrder(String name, BasicUser buyer, double price, Restaurant restaurant, OrderStatus orderStatus) {
        this.name = name;
        this.buyer = buyer;
        this.price = price;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
    }

    public FoodOrder(String name, double price, BasicUser buyer, List<Cuisine> items, Restaurant restaurant, Driver driver) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.items = items;
        this.restaurant = restaurant;
        this.driver = driver;
        this.orderStatus = OrderStatus.PENDING;
    }

    public int getId() {
        return id;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public Chat getChat() {
        return chat;
    }
    public List<Cuisine> getItems() {
        return items;
    }
    public double getPrice() {
        return price;
    }
    public String getName() {
        return name;
    }
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public BasicUser getBuyer() {return buyer;}
    public Driver getDriver() {
        return driver;
    }

    @Override
    public String toString() {
        return name + " " + price;
    }
}
