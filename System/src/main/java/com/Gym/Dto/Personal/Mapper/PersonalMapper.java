package com.Gym.Dto.Personal.Mapper;


import com.Gym.Dto.Personal.PersonalAdminResponse;
import com.Gym.Model.Users_Models.Personal.Personal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalMapper {

    PersonalAdminResponse EntityToAdminResponse(Personal personal);
}
