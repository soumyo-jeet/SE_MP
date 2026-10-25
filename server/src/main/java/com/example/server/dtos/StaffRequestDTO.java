package com.example.server.dtos;

import com.example.server.enums.StaffDeptartment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffRequestDTO {
    @Size(min = 5, max = 10,
            message = "Username must be between 5 and 10 characters long.")
    private String username;

    @Size(min = 8,
            message = "Password must be at least 8 characters long.")
    private String password;

    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotNull(message = "Please select a department.")
    private StaffDeptartment department;
}
