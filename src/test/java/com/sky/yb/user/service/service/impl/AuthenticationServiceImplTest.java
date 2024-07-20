package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.JwtResponse;
import com.sky.yb.user.service.dto.UserAuthenticationDto;
import com.sky.yb.user.service.dto.UserRegistrationDto;
import com.sky.yb.user.service.exception.UserAlreadyExistException;
import com.sky.yb.user.service.mapper.UserMapper;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.repository.UserRepository;
import com.sky.yb.user.service.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserDetailsService userService;
    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl target;

    @Test
    public void withValidUserDetails_onRegisterUser_shouldRegisterUserSuccessfully() {
        // given
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setPassword("password");

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .password(userRegistrationDto.getPassword())
                .build();

        when(userRepository.existsByEmail(userRegistrationDto.getEmail())).thenReturn(false);
        when(mapper.mapToUser(userRegistrationDto)).thenReturn(user);
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("encodedPassword");

        // when
        target.registerUser(userRegistrationDto);

        // then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void withExistingEmail_onRegisterUser_shouldThrowUserAlreadyExistException() {
        // given
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("existing@example.com");
        userRegistrationDto.setPassword("password");

        when(userRepository.existsByEmail(userRegistrationDto.getEmail())).thenReturn(true);

        // when
        Exception exception = assertThrows(UserAlreadyExistException.class,
                () -> target.registerUser(userRegistrationDto));

        // then
        String expectedMessage = "User with provided email already exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void withValidCredentials_onCreateAuthenticationToken_shouldReturnJwtResponse() {
        // given
        UserAuthenticationDto authenticationRequest = new UserAuthenticationDto();
        authenticationRequest.setEmail("test@example.com");
        authenticationRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        String token = "dummyToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.loadUserByUsername(authenticationRequest.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        // when
        JwtResponse jwtResponse = target.createAuthenticationToken(authenticationRequest);

        // then
        assertNotNull(jwtResponse);
        assertEquals(token, jwtResponse.getJwtToken());
    }

    @Test
    public void withInvalidCredentials_onCreateAuthenticationToken_shouldThrowAuthenticationCredentialsNotFoundException() {
        // given
        UserAuthenticationDto authenticationRequest = new UserAuthenticationDto();
        authenticationRequest.setEmail("invalid@example.com");
        authenticationRequest.setPassword("wrongpassword");

        doThrow(new AuthenticationCredentialsNotFoundException("Invalid email or password"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // when
        Exception exception = assertThrows(AuthenticationCredentialsNotFoundException.class,
                () -> target.createAuthenticationToken(authenticationRequest));

        // then
        String expectedMessage = "Invalid email or password";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}
