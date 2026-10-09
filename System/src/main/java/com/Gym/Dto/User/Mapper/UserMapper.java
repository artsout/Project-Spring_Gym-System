package com.Gym.Dto.User.Mapper;


import com.Gym.Dto.User.UserAdminResponseDto;
import com.Gym.Model.Users_Models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserAdminResponseDto toAdminResponse(User user);
}
