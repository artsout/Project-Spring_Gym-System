package com.Gym.Dto.User.Auth;


import com.Gym.Model.Users_Models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAuthMapper {



    User toEntity(UserLoginDto userLoginDto);


}


