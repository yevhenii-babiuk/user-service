package com.sky.yb.user.service.service;

import com.sky.yb.user.service.dto.UserExternalProjectDto;
import com.sky.yb.user.service.model.UserExternalProject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserExternalProjectService {

    @Transactional
    UserExternalProject addUserExternalProject(String userId,
                                               UserExternalProjectDto projectDto);

    List<UserExternalProject> getProjects(String userId);

}
