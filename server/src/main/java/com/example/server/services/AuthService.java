package com.example.server.services;

import com.example.server.dtos.LoginRequestDTO;
import com.example.server.dtos.LoginResponseDTO;
import com.example.server.models.UserDetailsImpl;
import com.example.server.utils.AuthUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
    AuthenticationManager authenticationManager;
    AuthUtil authUtil;
    OTPService otpService;
    RedisService redisService;
    EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager, AuthUtil authUtil,
                       OTPService otpService, RedisService redisService,
                       EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.otpService = otpService;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    @Transactional
    public LoginResponseDTO login (LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword())
        );


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(userDetails);

        return new LoginResponseDTO(token, userDetails.getId(), userDetails.getRole());
    }


    public void sendOtp (String email) {
        System.out.println("Received email: " + email);

        String exOtp = redisService.get(email);
        if(exOtp != null)
            throw new RuntimeException("OTP is already sent. You can request for new one after 10 mins.");

        String otp = OTPService.generateOTP(6);

        try{
            emailService.sendOtpEmail(
                    email,
                    "User",
                    otp,
                    10
            );
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        redisService.set(email, otp, 10L);
    }

    public Boolean verifyEmail (String email, String otp) {
        String otpSent = redisService.get(email);
        if(otpSent == null)
            throw new RuntimeException("OTP is invalid or has been expired.");
        System.out.println("Otp is sent: " + otpSent);
        System.out.println("Otp received: " + otp);
        if(otpSent.equals(otp))
            return true;
        return false;
    }
}
