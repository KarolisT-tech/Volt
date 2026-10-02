package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant extends BasicUser {
    private List<Cuisine> menu;
    private List<FoodOrder> foodOrders;
    protected String workHours;
    protected double rating;

    public Restaurant(String login, String password, String name, String surname, String phoneNumber, String address, String workHours) {
        super(login, password, name, surname, phoneNumber, address);
        this.workHours = workHours;
        this.menu = new ArrayList<>();
        this.foodOrders = new ArrayList<>();
    }


}
