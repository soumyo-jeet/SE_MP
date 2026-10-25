package com.example.server.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(message = "Please provide your valid email address.")
    private String email;

    @NotEmpty(message = "Please provide your valid password.")
    private String password;
}
