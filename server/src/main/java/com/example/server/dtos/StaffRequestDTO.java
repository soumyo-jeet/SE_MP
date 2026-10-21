package com.example.server.dtos;

import com.example.server.enums.StaffDeptartment;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffRequestDTO {
    private String username;
    private String password;
    private String email;
    private StaffDeptartment department;
}
