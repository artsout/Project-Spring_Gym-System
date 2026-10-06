package com.Gym.config;


import com.Gym.Model.Enum.TypeOfRole;
import com.Gym.Model.Users_Models.Personal.PersonalRole;
import com.Gym.Repository.Personal.PersonalRepository;
import com.Gym.Repository.Personal.PersonalRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdminPersonalConfig implements CommandLineRunner {
    private final PersonalRepository personalRepository;
    private final PersonalRoleRepository personalRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = personalRoleRepository.findByTypeOfRole(TypeOfRole.ADMIN)
                .orElseGet(() -> personalRoleRepository.save(new PersonalRole(TypeOfRole.ADMIN)));;

        var userAdmin = personalRepository.findByRoles(roleAdmin);

    }
}
