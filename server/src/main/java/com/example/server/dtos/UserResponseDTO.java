package com.example.server.dtos;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String password;
    private String email;

    public UserResponseDTO(Long id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
