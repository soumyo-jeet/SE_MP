package com.example.server.dtos;

import com.example.server.enums.StaffDeptartment;
import com.example.server.enums.StaffStatus;
import com.example.server.models.Staff;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffResponseDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private StaffStatus status;
    private StaffDeptartment department;

    public static StaffResponseDTO getStaffResponseDTO (Staff staff) {
        StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
        staffResponseDTO.setId(staff.getId());
        staffResponseDTO.setUsername(staff.getUsername());
        staffResponseDTO.setPassword(staff.getPassword());
        staffResponseDTO.setEmail(staff.getEmail());
        staffResponseDTO.setStatus(staff.getStatus());
        staffResponseDTO.setDepartment(staff.getDepartment());
        return staffResponseDTO;
    }
}
