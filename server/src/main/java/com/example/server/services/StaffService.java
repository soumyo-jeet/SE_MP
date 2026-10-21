package com.example.server.services;


import com.example.server.dtos.StaffRequestDTO;
import com.example.server.enums.StaffDeptartment;
import com.example.server.enums.StaffStatus;
import com.example.server.models.Admin;
import com.example.server.models.Staff;
import com.example.server.models.Ticket;
import com.example.server.models.User;
import com.example.server.repositories.AdminRepository;
import com.example.server.repositories.StaffRepository;
import com.example.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    StaffRepository staffRepository;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    AdminRepository adminRepository;

    public StaffService(
            StaffRepository staffRepository, PasswordEncoder passwordEncoder,
            UserRepository userRepository, AdminRepository adminRepository) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Staff createStaff(StaffRequestDTO staffRequestDTO) {
        Optional<Staff> exStaff = staffRepository.findByEmail(staffRequestDTO.getEmail());
        Optional<User> exUser = userRepository.findByEmail(staffRequestDTO.getEmail());
        Optional<Admin> exAdmin = adminRepository.findByEmail(staffRequestDTO.getEmail());

        if (exStaff.isPresent() || exUser.isPresent() || exAdmin.isPresent())
            throw new RuntimeException("Account with this email already exists");

        Staff staff = new Staff();
        staff.setUsername(staffRequestDTO.getUsername());
        staff.setPassword(passwordEncoder.encode(staffRequestDTO.getPassword()));
        staff.setEmail(staffRequestDTO.getEmail());
        staff.setDepartment(staffRequestDTO.getDepartment());
        staff.setStatus(StaffStatus.ACTIVE);

        return staffRepository.save(staff);
    }

    @Transactional
    public List<Staff> findActiveStaffsByDepartment(StaffDeptartment department) {
        List<Staff> staffs = staffRepository.findByDepartmentAndStatus(department, StaffStatus.ACTIVE);
        return staffs;
    }

    @Transactional
    public Staff findById(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new RuntimeException("Staff not found"));
    }

    @Transactional
    public Staff updateStatus(Long staffId, StaffStatus status) {
        Staff staff = findById(staffId);
        staff.setStatus(status);
        return staff;
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
}
