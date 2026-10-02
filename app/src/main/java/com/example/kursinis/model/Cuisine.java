package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cuisine implements Serializable {

    private int id;
    protected String name;
    protected Double price;

    private List<FoodOrder> orderList;
    private Restaurant restaurant;
    private List<Ingridients> ingredients;


    public Cuisine(String name, List<Ingridients> ingredients, Double price, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " " + price;
    }
}
