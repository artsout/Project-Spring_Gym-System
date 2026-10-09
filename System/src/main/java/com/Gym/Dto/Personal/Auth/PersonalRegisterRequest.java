package com.Gym.Dto.Personal.Auth;


import com.Gym.Model.Address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalRegisterRequest {
    @NotBlank
    @Size(min = 5 , max = 100)
    private String completeName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 6,max = 6)
    private String cref;

    @NotBlank
    @Size(min = 5,max = 20)
    private String password;

    private Address address;

    @NotBlank
    @Size(min = 5,max = 100)
    private String specialization;
}
