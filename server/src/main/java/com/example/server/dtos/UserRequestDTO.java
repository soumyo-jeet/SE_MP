package com.example.server.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
}
