package com.sky.yb.user.service.service;

import com.sky.yb.user.service.dto.UserDto;
import com.sky.yb.user.service.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {
    @Transactional
    User createUser(UserDto userDto);

    Optional<User> getUser(String id);

    void deleteUser(String id);

    @Transactional
    User updateUser(String id,
                    UserDto userDto);
}
