package com.DTO;

import lombok.Getter;

@Getter
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String role;

    public UserResponseDTO(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

}
