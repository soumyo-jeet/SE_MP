package com.example.server.controllers;

import com.example.server.dtos.LoginRequestDTO;
import com.example.server.dtos.LoginResponseDTO;
import com.example.server.dtos.UserRequestDTO;
import com.example.server.dtos.UserResponseDTO;
import com.example.server.models.Admin;
import com.example.server.models.User;
import com.example.server.services.AdminService;
import com.example.server.services.AuthService;
import com.example.server.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;
    private AdminService adminService;

    public AuthController(AuthService authService, UserService userService,  AdminService adminService) {
        this.authService = authService;
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return  ResponseEntity.ok(loginResponseDTO);
    }

    // Only for users
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserRequestDTO userRequestDTO) {
        User newUser = userService.createUser(userRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO(newUser.getId(),
                newUser.getUsername(),
                newUser.getEmail(),
                newUser.getPassword());

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    // admin creation
    @PostMapping("/admin/signup")
    public ResponseEntity<UserResponseDTO> createAdmin(
            @RequestBody UserRequestDTO userRequestDTO) {
        Admin newAdmin = adminService.createAdmin(userRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO(newAdmin.getId(),
                newAdmin.getUsername(),
                newAdmin.getEmail(),
                newAdmin.getPassword());

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

}
