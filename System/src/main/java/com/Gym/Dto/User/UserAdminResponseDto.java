package com.Gym.Dto.User;


import com.Gym.Model.Address.Address;
import com.Gym.Model.Users_Models.Payment;
import com.Gym.Model.Users_Models.Plan;

import java.util.UUID;

public record UserAdminResponseDto(UUID id, String completeName, String email , String matricula, String perfilImageUrl,
                                   Address address, Plan userPlan){
}
