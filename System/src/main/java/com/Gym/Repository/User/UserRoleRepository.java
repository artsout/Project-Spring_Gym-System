package com.Gym.Repository.User;


import com.Gym.Model.Enum.TypeOfRole;
import com.Gym.Model.Users_Models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    Optional<UserRole> findByUserRole(TypeOfRole userRole);

}
