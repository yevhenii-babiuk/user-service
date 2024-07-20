package com.sky.yb.user.service.service.impl;

import com.sky.yb.user.service.dto.UserExternalProjectDto;
import com.sky.yb.user.service.model.User;
import com.sky.yb.user.service.model.UserExternalProject;
import com.sky.yb.user.service.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserExternalProjectServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserExternalProjectServiceImpl target;

    @Test
    public void withValidUserId_onAddUserExternalProject_shouldAddProjectSuccessfully() {
        // given
        String userId = "validUserId";
        UserExternalProjectDto projectDto = new UserExternalProjectDto();
        projectDto.setName("New Project");

        User user = User.builder()
                .id(userId)
                .userExternalProjects(new ArrayList<>())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserExternalProject addedProject = target.addUserExternalProject(userId, projectDto);

        // then
        assertNotNull(addedProject);
        assertEquals("New Project", addedProject.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void withInvalidUserId_onAddUserExternalProject_shouldThrowUsernameNotFoundException() {
        // given
        String userId = "invalidUserId";
        UserExternalProjectDto projectDto = new UserExternalProjectDto();
        projectDto.setName("New Project");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> target.addUserExternalProject(userId, projectDto));

        // then
        String expectedMessage = "User with provided ID does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void withValidUserId_onGetProjects_shouldReturnProjectsList() {
        // given
        String userId = "validUserId";
        User user = User.builder()
                .id(userId)
                .build();
        UserExternalProject userExternalProject = UserExternalProject.builder()
                .id(new ObjectId().toHexString())
                .name("Project 1")
                .build();

        List<UserExternalProject> projects = new ArrayList<>();
        projects.add(userExternalProject);
        user.setUserExternalProjects(projects);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findByIdWithExternalProjects(userId)).thenReturn(user);

        // when
        List<UserExternalProject> returnedProjects = target.getProjects(userId);

        // then
        assertNotNull(returnedProjects);
        assertEquals(1, returnedProjects.size());
        assertEquals("Project 1", returnedProjects.getFirst().getName());
    }

    @Test
    public void withInvalidUserId_onGetProjects_shouldThrowUsernameNotFoundException() {
        // given
        String userId = "invalidUserId";

        when(userRepository.existsById(userId)).thenReturn(false);

        // when
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> target.getProjects(userId));

        // then
        String expectedMessage = "User with provided ID does not exist";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}
