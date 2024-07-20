package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoBasedUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MongoBasedUserDetailsService target;

    @Test
    public void withExistingUsername_onLoadUserByUsername_shouldReturnUserDetails() {
        // given
        String username = "test@example.com";
        User user = User.builder()
                .email(username)
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        // when
        UserDetails userDetails = target.loadUserByUsername(username);

        // then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void withNonExistingUsername_onLoadUserByUsername_shouldThrowUsernameNotFoundException() {
        // given
        String username = "nonExisting@example.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> target.loadUserByUsername(username));

        // then
        String expectedMessage = String.format("User: %s, not found", username);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}