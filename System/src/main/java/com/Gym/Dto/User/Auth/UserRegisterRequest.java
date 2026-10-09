package com.Gym.Dto.User.Auth;

import com.Gym.Model.Address.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 5 , max = 50)
    private String completeName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 5,max = 20)
    private String password;

    @NotBlank
    @CPF
    private String cpf;

    @NotNull
    private LocalDateTime userCreationDate;

    private Address address;


}
