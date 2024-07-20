package com.sky.yb.user.service.service;

import com.sky.yb.user.service.dto.JwtResponse;
import com.sky.yb.user.service.dto.UserAuthenticationDto;
import com.sky.yb.user.service.dto.UserRegistrationDto;

public interface AuthenticationService {

    void registerUser(UserRegistrationDto userRegistrationDTO);

    JwtResponse createAuthenticationToken(UserAuthenticationDto authenticationRequest);
}
