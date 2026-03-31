package com.example.medicalai.api.controller;

import com.example.medicalai.api.dto.LoginRequest;
import com.example.medicalai.api.dto.LoginResponse;
import com.example.medicalai.api.dto.ResponseData;
import com.example.medicalai.api.security.JwtService;
import com.example.medicalai.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userservice;

    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody LoginRequest loginrequest) {

        if (loginrequest != null && loginrequest.getUsername() != null && !loginrequest.getUsername().isEmpty() && loginrequest.getPassword() != null && !loginrequest.getPassword().isEmpty()) {

            return userservice.login(loginrequest.getUsername(), loginrequest.getPassword());
        } else {
            return ResponseEntity.badRequest().body(new ResponseData("Invalid login request", 400, "Failed"));
        }
    }
}
