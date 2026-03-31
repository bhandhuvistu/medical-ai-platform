package com.example.medicalai.api.service;

import com.example.medicalai.api.dto.LoginResponse;
import com.example.medicalai.api.dto.ResponseData;
import com.example.medicalai.api.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authManager;

    private final  JwtService jwtService;

    public UserService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<ResponseData> login(String username, String password) {

        try {
            if (username == null || username.isBlank() ||
                    password == null || password.isBlank()) {

                return ResponseEntity.badRequest()
                        .body(new ResponseData("Invalid login request", 400, "Failed"));
            }

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails ud = (UserDetails) auth.getPrincipal();

            List<String> roles = new ArrayList<>();
            for (GrantedAuthority ga : ud.getAuthorities()) {
                roles.add(ga.getAuthority().replace("ROLE_", ""));
            }

            String token = jwtService.issueToken(ud.getUsername(), roles);

            return ResponseEntity.ok(
                    new ResponseData("Successfully Login", 200, "Success",
                            new LoginResponse(token, roles))
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401)
                    .body(new ResponseData("Invalid username or password", 401, "Failed"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseData("An error occurred during login", 500, "Failed"));
        }
    }
}