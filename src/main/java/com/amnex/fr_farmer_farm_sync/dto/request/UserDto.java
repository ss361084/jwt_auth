package com.amnex.fr_farmer_farm_sync.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank(message = "username is required")
    private String userName;
    @NotBlank(message = "password is required")
    private String password;
}
