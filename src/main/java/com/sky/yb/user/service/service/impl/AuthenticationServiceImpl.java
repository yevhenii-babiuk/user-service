package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.JwtResponse;
import com.sky.yb.user.service.dto.UserAuthenticationDto;
import com.sky.yb.user.service.dto.UserRegistrationDto;
import com.sky.yb.user.service.exception.UserAlreadyExistException;
import com.sky.yb.user.service.mapper.UserMapper;
import com.sky.yb.user.service.model.Role;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.repository.UserRepository;
import com.sky.yb.user.service.service.AuthenticationService;
import com.sky.yb.user.service.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Role DEFAULT_ROLE = Role.ROLE_USER;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto userRegistrationDTO) {
        boolean isUserAlreadyExist = userRepository.existsByEmail(userRegistrationDTO.getEmail());
        if (isUserAlreadyExist) {
            log.info(String.format("User %s provided email already exist", userRegistrationDTO.getEmail()));
            throw new UserAlreadyExistException("User with provided email already exist");
        }

        User newUser = mapper.mapToUser(userRegistrationDTO);
        newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        newUser.setUserRoles(Set.of(DEFAULT_ROLE));

        userRepository.save(newUser);
    }

    public JwtResponse createAuthenticationToken(UserAuthenticationDto authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            log.info(String.format("Unable to authenticate %s", authenticationRequest.getEmail()));
            throw new AuthenticationCredentialsNotFoundException("Invalid email or password", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);

        return JwtResponse.builder()
                .jwtToken(token)
                .build();
    }

}
