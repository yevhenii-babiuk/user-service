package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.UserExternalProjectDto;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.model.UserExternalProject;
import com.sky.yb.user.service.repository.UserRepository;
import com.sky.yb.user.service.service.UserExternalProjectService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserExternalProjectServiceImpl implements UserExternalProjectService {
    private final UserRepository userRepository;

    @Transactional
    public UserExternalProject addUserExternalProject(String userId,
                                                      UserExternalProjectDto projectDto) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserExternalProject project = UserExternalProject.builder()
                    .id(new ObjectId().toHexString())
                    .name(projectDto.getName())
                    .build();

            User userToSave = optionalUser.get();
            userToSave.addExternalProjects(project);

            userRepository.save(userToSave);
            return project;
        } else {
            throw new UsernameNotFoundException("User with provided ID does not exist");
        }
    }

    public List<UserExternalProject> getProjects(String userId) {
        boolean isUserExist = userRepository.existsById(userId);

        if (isUserExist) {
            User us = userRepository.findByIdWithExternalProjects(userId);
            return us.getUserExternalProjects();
        } else {
            throw new UsernameNotFoundException("User with provided ID does not exist");
        }
    }

}
