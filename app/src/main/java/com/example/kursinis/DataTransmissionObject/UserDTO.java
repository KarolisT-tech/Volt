package com.example.kursinis.DataTransmissionObject;

public class UserDTO {
    private int id;
    private String type;

    public UserDTO(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
