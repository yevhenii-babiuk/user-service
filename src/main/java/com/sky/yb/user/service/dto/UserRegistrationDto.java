package com.sky.yb.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email(message = "Email should have valid format")
    private String email;
    @NotBlank(message = "Password should not be empty")
    private String password;
    private String name;
}
