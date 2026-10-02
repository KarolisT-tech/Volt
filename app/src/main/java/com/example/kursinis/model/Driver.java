package com.example.kursinis.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver extends BasicUser{
    protected String licence;
    @SerializedName("bdate")
    protected LocalDate bDate;
    private VehicleType vehicleType;
    //private List<FoodOrder> orders;

    public Driver(String login, String password, String name, String surname, String phoneNumber, String address, String licence, LocalDate bDate, VehicleType vehicleType) {
        super(login, password, name, surname, phoneNumber, address);
        this.licence = licence;
        this.bDate = bDate;
        this.vehicleType = vehicleType;
    }

    public String getLicence() {
        return licence;
    }

    public String getbDate() {
        return bDate.toString();
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
