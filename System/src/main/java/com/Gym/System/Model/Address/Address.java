package com.Gym.System.Model.Address;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String cep;

    private String rua;

    private String cidade;

}
