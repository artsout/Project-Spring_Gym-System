package com.Gym.Dto.User.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto{

    @Email
    private  String email;

    @NotBlank
    @Size(min = 5,max = 20)
    private String password;
}
