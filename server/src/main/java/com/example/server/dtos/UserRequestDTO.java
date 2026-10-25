package com.example.server.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDTO {

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 5, max = 10,
            message = "Username must be between 5 and 10 characters long.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8,
            message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Email address cannot be empty.")
    @Email(message = "Please enter a valid email address.")
    private String email;
}