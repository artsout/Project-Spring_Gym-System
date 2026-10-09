package com.Gym.Controller.User;


import com.Gym.Dto.User.Auth.UserRegisterRequest;
import com.Gym.Dto.User.Mapper.UserMapper;
import com.Gym.Dto.User.UserAdminResponseDto;
import com.Gym.Model.Users_Models.User;
import com.Gym.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_ADMIN')")
    public ResponseEntity<UserAdminResponseDto> findById(@PathVariable UUID userId){
        User user =userService.findById(userId);
        UserAdminResponseDto userResponse = userMapper.toAdminResponse(user);
        return ResponseEntity.ok(userResponse);
    }

}
