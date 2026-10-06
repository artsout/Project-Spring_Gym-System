package com.Gym.config;


import com.Gym.Model.Enum.TypeOfRole;
import com.Gym.Model.Users_Models.UserRole;
import com.Gym.Repository.User.UserRepository;
import com.Gym.Repository.User.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig implements CommandLineRunner {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = userRoleRepository.findByUserRole(TypeOfRole.ADMIN)
                .orElseGet(() -> userRoleRepository.save(new UserRole(TypeOfRole.ADMIN)));;

        var userAdmin = userRepository.findByRoles(roleAdmin);

    }
}
