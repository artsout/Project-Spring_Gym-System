package com.Gym.Controller;


import com.Gym.Dto.User.Auth.UserLoginDto;
import com.Gym.Dto.User.Auth.UserLoginResponse;
import com.Gym.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserLoginController {

    private final UserService userService;

    @PostMapping("/login")

    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginDto userLoginDto){

        var expiresIn = 900000L;
        log.info("Trying to login with email:{}",userLoginDto.getEmail());
        String token = userService.login(userLoginDto,expiresIn);

        log.info("login made with success");
        return ResponseEntity.ok(new UserLoginResponse(token,expiresIn));
    }
}
