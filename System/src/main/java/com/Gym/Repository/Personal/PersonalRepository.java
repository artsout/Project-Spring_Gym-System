package com.Gym.Repository.Personal;


import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Model.Users_Models.Personal.PersonalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, UUID> {

    List<Personal> findByRoles(PersonalRole personalRole);

   Optional<Personal> findByEmail(String personalEmail);

    Optional<Personal> findByCref(String personalCref);
}
