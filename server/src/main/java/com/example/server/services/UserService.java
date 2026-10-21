package com.example.server.services;


import com.example.server.dtos.UserRequestDTO;
import com.example.server.models.Admin;
import com.example.server.models.Staff;
import com.example.server.models.Ticket;
import com.example.server.models.User;
import com.example.server.repositories.AdminRepository;
import com.example.server.repositories.StaffRepository;
import com.example.server.repositories.TicketRepository;
import com.example.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private StaffRepository staffRepository;
    private AdminRepository adminRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       StaffRepository staffRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public User createUser(UserRequestDTO userRequestDTO) {
        Optional<Staff> exStaff = staffRepository.findByEmail(userRequestDTO.getEmail());
        Optional<User> exUser = userRepository.findByEmail(userRequestDTO.getEmail());
        Optional<Admin> exAdmin = adminRepository.findByEmail(userRequestDTO.getEmail());

        if (exStaff.isPresent() || exUser.isPresent() || exAdmin.isPresent())
            throw new RuntimeException("Account with this email already exists");

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setEmail(userRequestDTO.getEmail());

        User newUser = userRepository.save(user);
        return newUser;
    }

    @Transactional
    public User findById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public List<Ticket> findAllTickets(Long userId) {
        User user = findById(userId);
        return user.getTickets();
    }
}
