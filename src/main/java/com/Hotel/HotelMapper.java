package com.Hotel;


import com.Amenity.Amenity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelRequestDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName() == null ? null : dto.getName().trim());
        hotel.setAddress(dto.getAddress() == null ? null : dto.getAddress().trim());
        hotel.setCity(dto.getCity());
        hotel.setCountry(dto.getCountry());
        hotel.setPhone(dto.getPhone());
        hotel.setEmail(dto.getEmail());
        hotel.setDescription(dto.getDescription());
        hotel.setStarRating(dto.getStarRating());
        hotel.setNumberOfFloors(dto.getNumberOfFloors());
        return hotel;
    }

    public void updateEntity(Hotel hotel, HotelRequestDTO dto) {
        if (dto.getName() != null) hotel.setName(dto.getName().trim());
        if (dto.getAddress() != null) hotel.setAddress(dto.getAddress().trim());
        if (dto.getCity() != null) hotel.setCity(dto.getCity());
        if (dto.getCountry() != null) hotel.setCountry(dto.getCountry());
        if (dto.getPhone() != null) hotel.setPhone(dto.getPhone());
        if (dto.getEmail() != null) hotel.setEmail(dto.getEmail());
        if (dto.getDescription() != null) hotel.setDescription(dto.getDescription());
        if (dto.getStarRating() != null) hotel.setStarRating(dto.getStarRating());
        if (dto.getNumberOfFloors() != null) hotel.setNumberOfFloors(dto.getNumberOfFloors());
    }

    public HotelResponseDTO toResponseDTO(Hotel hotel) {
        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getCity(),
                hotel.getCountry(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getDescription(),
                hotel.getStarRating(),
                hotel.getNumberOfFloors(),
                hotel.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet())
        );
    }
}