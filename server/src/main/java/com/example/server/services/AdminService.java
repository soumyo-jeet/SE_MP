package com.example.server.services;

import com.example.server.dtos.UserRequestDTO;
import com.example.server.models.Admin;
import com.example.server.models.Staff;
import com.example.server.models.User;
import com.example.server.repositories.AdminRepository;
import com.example.server.repositories.StaffRepository;
import com.example.server.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private StaffRepository staffRepository;
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder,
                        StaffRepository staffRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    public Admin createAdmin(UserRequestDTO userRequestDTO) {
        Optional<Staff> exStaff = staffRepository.findByEmail(userRequestDTO.getEmail());
        Optional<User> exUser = userRepository.findByEmail(userRequestDTO.getEmail());
        Optional<Admin> exAdmin = adminRepository.findByEmail(userRequestDTO.getEmail());

        if (exStaff.isPresent() || exUser.isPresent() || exAdmin.isPresent())
            throw new RuntimeException("Account with this email already exists");

        Admin admin = new Admin();
        admin.setUsername(userRequestDTO.getUsername());
        admin.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        admin.setEmail(userRequestDTO.getEmail());
        admin.setDefPass(userRequestDTO.getPassword());

        return adminRepository.save(admin);
    }
}
