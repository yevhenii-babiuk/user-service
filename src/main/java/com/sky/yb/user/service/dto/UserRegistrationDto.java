package com.sky.yb.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email(message = "Email should have valid format")
    private String email;
    @NotBlank(message = "Password should not be empty")
    @Size(min = 8, message = "Password should contains 8 symbols at least")
    private String password;
    private String name;
}
