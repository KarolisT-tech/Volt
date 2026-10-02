package com.example.kursinis.utils;

import com.example.kursinis.model.Cuisine;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private Integer restaurantId = null;
    private final List<Cuisine> items = new ArrayList<>();

    public List<Cuisine> getItems() {
        return items;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer id) {
        this.restaurantId = id;
    }

    public void addItem(Cuisine c) {
        items.add(c);
    }

    public void removeItem(Cuisine c) {
        items.remove(c);
    }

    public void clear() {
        items.clear();
        restaurantId = null;
    }

}
