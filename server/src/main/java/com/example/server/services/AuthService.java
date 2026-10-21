package com.example.server.services;

import com.example.server.dtos.LoginRequestDTO;
import com.example.server.dtos.LoginResponseDTO;
import com.example.server.models.UserDetailsImpl;
import com.example.server.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    AuthenticationManager authenticationManager;
    AuthUtil authUtil;

    public AuthService(AuthenticationManager authenticationManager, AuthUtil authUtil) {
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
    }

    public LoginResponseDTO login (LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword())
        );


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(userDetails);

        return new LoginResponseDTO(token, userDetails.getId(), userDetails.getRole());
    }
}
