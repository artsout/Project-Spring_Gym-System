package com.Gym.Service;

import com.Gym.Dto.Personal.Auth.PersonalLoginDto;
import com.Gym.Dto.Personal.Auth.PersonalRegisterRequest;
import com.Gym.Exception.MethodParameterNull;
import com.Gym.Exception.ObjectNotFound;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Repository.Personal.PersonalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password4j.Argon2Password4jPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalService {


    private final PersonalRepository personalRepository;
    private final Argon2Password4jPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public Personal findById(UUID personalId){
        if (personalId==null){
            throw  new MethodParameterNull("userId cant be null");
        }
        log.info("Searching personal with ID: {}", personalId);

        return personalRepository.findById(personalId)
                .orElseThrow(()-> new ObjectNotFound("Object Not Found"));
    }

    public void register(PersonalRegisterRequest personalRegisterRequest){
        if (personalRegisterRequest==null){
            throw  new MethodParameterNull("parameter cant be null");
        }
        if (personalRepository.findByEmail(personalRegisterRequest.getEmail()).isPresent()) {
            throw new com.Gym.Exception.BusinessException("Email already exist!");

        }
        if (personalRepository.findByCref(personalRegisterRequest.getCref()).isPresent()) {
            throw new com.Gym.Exception.BusinessException("CREF already exist!");
        }
        Personal personal = new Personal();
        personal.setCompleteName(personalRegisterRequest.getCompleteName());
        personal.setEmail(personalRegisterRequest.getEmail());
        personal.setPersonalCreationDate(LocalDateTime.now());
        personal.setCref(personalRegisterRequest.getCref());
        personal.setAddress(personalRegisterRequest.getAddress());
        personal.setSpecialization(personalRegisterRequest.getSpecialization());


        personalRepository.save(personal);
    }

    public String login(PersonalLoginDto personalLoginDto , Long expiresIn){
        if (personalLoginDto ==null || expiresIn ==null){
            throw  new MethodParameterNull("parameter cant be null");
        }
        String personalEmail = personalLoginDto.getEmail();
        String personalCref = personalLoginDto.getCref();

        Personal personalValidation1 = findByEmail(personalEmail);

        Personal personalValidation2 = findByCref(personalCref);

        if(!personalValidation1.equals(personalValidation2) || !personalValidation1.isPasswordCorrect(personalLoginDto.getPassword() , passwordEncoder)){
            log.warn("Login failed: Wrong password for email {} and cref :{}", personalEmail ,personalCref);
            throw new BadCredentialsException("Invalid email , password or cref");
        }

        var scope = personalValidation1.getRoles()
                .stream()
                .map(personalRole-> personalRole.getTypeOfRole().name())
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(personalValidation1.getId().toString())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .claim("scope", scope)
                .build();

        String jwtValues = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return  jwtValues;
    }



    public Personal findByEmail(String personalEmail){
        if (personalEmail==null){
            throw  new MethodParameterNull("parameter cant be null");
        }
        log.info("Searching personal with email: {}", personalEmail);

        return personalRepository.findByEmail(personalEmail)
                .orElseThrow(()-> new ObjectNotFound("Object Not Found"));
    }
    public  Personal findByCref(String personalCref){
        if (personalCref==null){
            throw  new MethodParameterNull("parameter cant be null");
        }
        log.info("Searching personal with cref: {}", personalCref);

        return personalRepository.findByCref(personalCref)
                .orElseThrow(()-> new ObjectNotFound("Object Not Found"));

    }



}
