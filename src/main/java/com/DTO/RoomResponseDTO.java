package com.DTO;
import jakarta.validation.constraints.*;
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private double price;

    public RoomResponseDTO(Long id, String roomNumber, double price) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
    }

    public Long getId() {return id;}
    public String getRoomNumber() {return roomNumber;}
    public double getPrice() {return price;}

}
