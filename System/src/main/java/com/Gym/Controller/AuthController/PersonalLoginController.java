package com.Gym.Controller.AuthController;


import com.Gym.Dto.Personal.Auth.PersonalLoginDto;
import com.Gym.Dto.Personal.Auth.PersonalLoginResponse;
import com.Gym.Service.PersonalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal")
@RequiredArgsConstructor
@Slf4j
public class PersonalLoginController {

    private final PersonalService personalService;

    public ResponseEntity<PersonalLoginResponse> login(@Valid @RequestBody PersonalLoginDto personalLoginDto){
        var expiresIn = 900000L;
        log.info("Trying to login with email:{} and cref:{}",personalLoginDto.getEmail() ,personalLoginDto.getCref());

        String token = personalService.login(personalLoginDto,expiresIn);
        log.info("login made with success");
        return ResponseEntity.ok(new PersonalLoginResponse(token,expiresIn));
    }
}
