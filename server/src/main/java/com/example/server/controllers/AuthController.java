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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return  ResponseEntity.ok(loginResponseDTO);
    }

    // Only for users
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
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
           @Valid @RequestBody UserRequestDTO userRequestDTO) {
        Admin newAdmin = adminService.createAdmin(userRequestDTO);
        UserResponseDTO userResponseDTO = new UserResponseDTO(newAdmin.getId(),
                newAdmin.getUsername(),
                newAdmin.getEmail(),
                newAdmin.getPassword());

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/otp")
    public ResponseEntity<String> getOtp (
            @RequestParam("email")  String email
    ) {
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP has been sent");
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> checkOtp(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp
    ) {
        return ResponseEntity.ok(authService.verifyEmail(email, otp));
    }
}
