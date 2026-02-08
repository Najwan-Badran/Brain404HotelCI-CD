package com.Mapper;

import com.DTO.*;
import com.Entity.*;

public class UserMapper {

    private UserMapper() {}

    public static User toEntity(UserRequestDTO dto, Role role) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);
        return user;
    }

    public static UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().getName()
        );
    }
}
