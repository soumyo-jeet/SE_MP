package com.example.server.repositories;

import com.example.server.enums.StaffDeptartment;
import com.example.server.enums.StaffStatus;
import com.example.server.models.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByDepartmentAndStatus(StaffDeptartment department, StaffStatus staffStatus);
    Optional<Staff> findByEmail(String email);
}
