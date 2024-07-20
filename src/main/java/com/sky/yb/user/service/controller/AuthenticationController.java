package com.sky.yb.user.service.controller;

import com.sky.yb.user.service.dto.JwtResponse;
import com.sky.yb.user.service.dto.UserAuthenticationDto;
import com.sky.yb.user.service.dto.UserRegistrationDto;
import com.sky.yb.user.service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth", produces = "application/json")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDTO) {
        authenticationService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserAuthenticationDto authenticationRequest) {
        JwtResponse response = authenticationService.createAuthenticationToken(authenticationRequest);
        return ResponseEntity.ok(response);
    }

}
