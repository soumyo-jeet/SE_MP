package com.example.server.services;

import com.example.server.enums.Role;
import com.example.server.models.Admin;
import com.example.server.models.Staff;
import com.example.server.models.User;
import com.example.server.models.UserDetailsImpl;
import com.example.server.repositories.AdminRepository;
import com.example.server.repositories.StaffRepository;
import com.example.server.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private StaffRepository staffRepository;
    private AdminRepository adminRepository;


    public UserDetailsServiceImpl(UserRepository userRepository, StaffRepository staffRepository,
                                  AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adminRepository.findByEmail(email)
                .map(admin -> fromAdmin(admin))
                .orElseGet(() ->
                        staffRepository.findByEmail(email)
                                .map(staff-> fromStaff(staff))
                                .orElseGet(() ->
                                        userRepository.findByEmail(email)
                                                .map(user -> fromUser(user))
                                                .orElseThrow(() ->
                                                        new UsernameNotFoundException("Account not found.")
                                )
                ));
    }

    private UserDetailsImpl fromUser(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Role.USER
        );
    }

    private UserDetailsImpl fromStaff(Staff staff) {
        return new UserDetailsImpl(
                staff.getId(),
                staff.getEmail(),
                staff.getPassword(),
                Role.STAFF
        );
    }

    private UserDetailsImpl fromAdmin(Admin admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                Role.ADMIN
        );
    }
}
