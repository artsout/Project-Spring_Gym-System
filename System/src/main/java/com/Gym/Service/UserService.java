package com.Gym.Service;

import com.Gym.Dto.User.Auth.UserLoginDto;
import com.Gym.Exception.MethodParameterNull;
import com.Gym.Exception.ObjectNotFound;
import com.Gym.Model.Users_Models.User;
import com.Gym.Repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    public User findById(UUID userId){
        if (userId==null){
            throw  new MethodParameterNull("userId cant be null");
        }
        log.info("Searching user with ID: {}", userId);


        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFound("User with id " + userId + " not found"));
        }

    public String login(UserLoginDto userLoginDto, Long expiresIn){
        if (userLoginDto==null ){
            throw  new MethodParameterNull("parameter cant be null");
        }
        String userEmail = userLoginDto.getEmail();
        log.info("Searching user with email: {}", userEmail);
        User user = findByEmail(userEmail);

        if(!user.isPasswordCorrect(userLoginDto.getPassword(),passwordEncoder)){
            log.warn("Login failed: Wrong password for email {}", userEmail);
            throw new BadCredentialsException("Invalid email or password");
        }
        var scope = user.getRoles()
                .stream()
                .map(userRole-> userRole.getUserRole().name())
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("scope", scope)
                .build();

        String jwtValues = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return  jwtValues;
    }

    public User findByEmail(String userEmail){
        if (userEmail==null){
            throw  new MethodParameterNull("parameter cant be null");
        }
        return userRepository
                .findByEmail(userEmail).orElseThrow(() -> new ObjectNotFound("User with email " + userEmail + " not found"));
    }

}
