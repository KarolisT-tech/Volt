package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Chat {

    private int id;
    private String name;
    private LocalDate dateCreated;

    private FoodOrder foodOrder;
    private List<Review> messages;

    public Chat(String name, FoodOrder foodOrder) {
        this.name = name;
        this.foodOrder = foodOrder;
        this.dateCreated = LocalDate.now();
        this.messages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }
}
