package com.Gym.Dto.Personal;

import com.Gym.Model.Address.Address;


public record PersonalAdminResponse(String completeName, String email, String perfilImageUrl, Address address,String specialization,String cref,Integer experienceYears) {
}
