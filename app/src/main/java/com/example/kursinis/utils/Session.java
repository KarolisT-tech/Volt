package com.example.kursinis.utils;

import com.example.kursinis.model.Cuisine;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Session instance;
    private int id;
    private String type;
    private CartManager cart = new CartManager();

    public static Session getInstance() {
        if(instance == null) instance = new Session();
        return instance;
    }

    public void setUser(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getUserId() {
        return id;
    }

    public String getUserType() {
        return type;
    }

    public void clear() {
        id = 0;
        type = null;
    }

    public CartManager getCart() {
        return cart;
    }
}
