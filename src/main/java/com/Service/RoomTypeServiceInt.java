package com.Service;

import com.DTO.RoomTypeRequestDTO;
import com.DTO.RoomTypeResponseDTO;

import java.util.List;

public interface RoomTypeServiceInt {

    RoomTypeResponseDTO create(RoomTypeRequestDTO dto);

    RoomTypeResponseDTO findById(Long id);

    List<RoomTypeResponseDTO> findAll();

    RoomTypeResponseDTO update(Long id, RoomTypeRequestDTO dto);

    void deleteById(Long id);
}
