package com.Gym.Dto.Personal.Auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalLoginDto {

    @Email
    private String email;

    @NotBlank
    @Size(min = 5 , max = 20)
    private String password;


    @NotBlank
    @Size(min = 6,max = 6)
    private String cref;
}
