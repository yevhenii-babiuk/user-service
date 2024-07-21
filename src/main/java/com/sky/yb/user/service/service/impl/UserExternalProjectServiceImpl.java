package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.UserExternalProjectDto;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.model.UserExternalProject;
import com.sky.yb.user.service.repository.UserRepository;
import com.sky.yb.user.service.service.UserExternalProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserExternalProjectServiceImpl implements UserExternalProjectService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserExternalProject addUserExternalProject(String userId,
                                                      UserExternalProjectDto projectDto) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserExternalProject project = UserExternalProject.builder()
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

    @Override
    public List<UserExternalProject> getProjects(String userId) {
        boolean isUserExist = userRepository.existsById(userId);

        if (isUserExist) {
            User user = userRepository.findByIdWithExternalProjects(userId);
            return user.getUserExternalProjects();
        } else {
            throw new UsernameNotFoundException("User with provided ID does not exist");
        }
    }

}
