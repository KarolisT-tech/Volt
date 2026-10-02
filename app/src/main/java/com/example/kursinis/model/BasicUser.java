package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicUser extends User{
    protected String address;
    //protected List<FoodOrder> myOrders;
    //protected List<Review> myReviews;

    public BasicUser(String login, String password, String name, String surname, String phoneNumber, String address) {
        super(login, password, name, surname, phoneNumber);
        this.address = address;
        //this.myReviews = new ArrayList<>();
        //this.myOrders = new ArrayList<>();
        //this.feedback = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }
}
