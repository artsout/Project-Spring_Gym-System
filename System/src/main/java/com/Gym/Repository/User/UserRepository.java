package com.Gym.Repository.User;

import com.Gym.Model.Users_Models.User;
import com.Gym.Model.Users_Models.UserRole;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <User, UUID>{

    List<User> findByRoles(UserRole userRole);


    Optional<User> findByEmail(String userEmail);

    Optional<Object> findByCpf( String cpf);
}
