package com.Gym.Repository.Personal;


import com.Gym.Model.Enum.TypeOfRole;
import com.Gym.Model.Users_Models.Personal.PersonalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalRoleRepository extends JpaRepository<PersonalRole,Long> {


    Optional<PersonalRole> findByTypeOfRole(TypeOfRole typeOfRole);
}
