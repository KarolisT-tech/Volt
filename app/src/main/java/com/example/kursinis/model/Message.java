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
public class Message {
    private int id;
    private String reviewText;
    private LocalDate dateCreated;

    private BasicUser messageOwner;
    private BasicUser messageReceiver;

    private Chat chat;

    public Message(String reviewText, BasicUser owner, BasicUser receiver, Chat chat) {
        this.reviewText = reviewText;
        this.messageOwner = owner;
        this.messageReceiver = receiver;
        this.chat = chat;
        this.dateCreated = LocalDate.now();
    }

//    @Override
//    public String toString() {
//        return "From: " + messageOwner.getName() + " " + messageOwner.getSurname() + " Text: " + reviewText;
//    }


    public int getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public BasicUser getMessageOwner() {
        return messageOwner;
    }
}
