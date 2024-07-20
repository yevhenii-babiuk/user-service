package com.sky.yb.user.service.mapper;

import com.sky.yb.user.service.dto.UserDto;
import com.sky.yb.user.service.dto.UserRegistrationDto;
import com.sky.yb.user.service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .userRoles(userDto.getUserRoles())
                .userExternalProjects(userDto.getUserExternalProjects())
                .build();
    }

    public User mapToUser(UserRegistrationDto userCreationDto) {
        return User.builder()
                .email(userCreationDto.getEmail())
                .password(userCreationDto.getPassword())
                .name(userCreationDto.getName())
                .build();
    }

}