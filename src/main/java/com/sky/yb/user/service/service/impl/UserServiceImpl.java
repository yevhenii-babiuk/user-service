package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.UserDto;
import com.sky.yb.user.service.exception.UserAlreadyExistException;
import com.sky.yb.user.service.mapper.UserMapper;
import com.sky.yb.user.service.model.Role;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.repository.UserRepository;
import com.sky.yb.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Role DEFAULT_ROLE = Role.ROLE_USER;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional
    public User createUser(UserDto userDto) {
        boolean isUserAlreadyExist = userRepository.existsByEmail(userDto.getEmail());
        if (isUserAlreadyExist) {
            log.info(String.format("User %s provided email already exist", userDto.getEmail()));
            throw new UserAlreadyExistException("User with provided email already exist");
        }

        User newUser = mapper.mapToUser(userDto);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setUserRoles(Set.of(DEFAULT_ROLE));

        return userRepository.save(newUser);
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(String id,
                           UserDto userDto) {
        boolean isUserExist = userRepository.existsById(id);
        if (!isUserExist) {
            throw new UsernameNotFoundException("User with provided ID does not exist");
        }

        User userToBeUpdated = mapper.mapToUser(userDto);
        if (userDto.getPassword() != null) {
            userToBeUpdated.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(userToBeUpdated);
    }

}
