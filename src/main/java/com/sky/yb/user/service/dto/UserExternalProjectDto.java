package com.sky.yb.user.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserExternalProjectDto {
    @NotBlank(message = "Project name should not be empty")
    private String name;
}