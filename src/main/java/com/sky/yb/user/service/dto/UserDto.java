package com.sky.yb.user.service.dto;

import com.sky.yb.user.service.model.Role;
import com.sky.yb.user.service.model.UserExternalProject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    @Email(message = "Email should have valid format")
    private String email;
    @NotBlank(message = "Password should not be empty")
    private String password;
    private String name;
    private List<UserExternalProject> userExternalProjects;
    private Set<Role> userRoles;
}
