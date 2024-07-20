package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.UserDto;
import com.sky.yb.user.service.exception.UserAlreadyExistException;
import com.sky.yb.user.service.mapper.UserMapper;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl target;

    @Test
    public void withNonExistingUser_onCreateUser_shouldCreateUserSuccessfully() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(mapper.mapToUser(userDto)).thenReturn(user);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // when
        User createdUser = target.createUser(userDto);

        // then
        assertNotNull(createdUser);
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void withExistingUser_onCreateUser_shouldThrowUserAlreadyExistException() {
        // given
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // when
        Exception exception = assertThrows(UserAlreadyExistException.class,
                () -> target.createUser(userDto));

        // then
        String expectedMessage = "User with provided email already exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void withExistingUserId_onGetUser_shouldReturnUser() {
        // given
        String userId = "userId";
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        Optional<User> returnedUser = target.getUser(userId);

        // then
        assertTrue(returnedUser.isPresent());
        assertEquals(userId, returnedUser.get().getId());
    }

    @Test
    public void withNonExistingUserId_onGetUser_shouldReturnEmptyOptional() {
        // given
        String userId = "nonExistingUserId";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Optional<User> returnedUser = target.getUser(userId);

        // then
        assertFalse(returnedUser.isPresent());
    }

    @Test
    public void withExistingUserId_onDeleteUser_shouldDeleteUserSuccessfully() {
        // given
        String userId = "userId";

        // when
        target.deleteUser(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void withNonExistingUserId_onUpdateUser_shouldThrowUsernameNotFoundException() {
        // given
        String userId = "nonExistingUserId";

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        when(userRepository.existsById(userId)).thenReturn(false);

        // when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> target.updateUser(userId, userDto));

        // then
        String expectedMessage = "User with provided ID does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void withExistingUserId_onUpdateUser_shouldUpdateUserSuccessfully() {
        // given
        String userId = "userId";

        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");

        User updatedUser = User.builder()
                .id(userId)
                .email(userDto.getEmail())
                .password("encodedNewPassword")
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(mapper.mapToUser(userDto)).thenReturn(updatedUser);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        // when
        User result = target.updateUser(userId, userDto);

        // then
        assertNotNull(result);
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals("encodedNewPassword", result.getPassword());
        verify(userRepository, times(1)).save(updatedUser);
    }

}
