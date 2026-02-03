package com.DTO;
import jakarta.validation.constraints.*;

public class RoomRequestDTO {
    @NotBlank
    private String roomNumber;
    @NotBlank
    private double price;


    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
