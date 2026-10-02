package com.example.kursinis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int id;
    private int rating;
    private String reviewText;
    private LocalDate dateCreated;

    private BasicUser commentOwner;
    private BasicUser feedbackOwner;
    private FoodOrder foodOrder;
    private Chat chat;

    public Review(String reviewText, int rating, BasicUser commentOwner, BasicUser feedbackOwner, FoodOrder foodOrder) {
        this.reviewText = reviewText;
        this.rating = rating;
        this.commentOwner = commentOwner;
        this.feedbackOwner = feedbackOwner;
        this.foodOrder = foodOrder;
        this.dateCreated = LocalDate.now();
    }

    public Review(String reviewText, BasicUser feedbackOwner, Chat chat) {
        this.reviewText = reviewText;
        this.feedbackOwner = feedbackOwner;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Sender: " + commentOwner.getName() + " " + commentOwner.getSurname() + " Review: " + reviewText + " ⭐ " + rating;
    }

}
