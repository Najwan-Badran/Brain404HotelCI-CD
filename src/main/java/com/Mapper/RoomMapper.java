package com.Mapper;

import com.DTO.*;
import com.Entity.Room;

public class RoomMapper {
    private RoomMapper() {}

    public static Room toEntity(RoomRequestDTO dto) {
        Room e = new Room();
        e.setRoomNumber(dto.getRoomNumber());
        e.setPrice(dto.getPrice());
        return e;
    }

    public static RoomResponseDTO toDto(Room e) {
        return new RoomResponseDTO(
                e.getId(),
                e.getRoomNumber(),
                e.getPrice()
        );
    }
}
