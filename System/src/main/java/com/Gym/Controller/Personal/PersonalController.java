package com.Gym.Controller.Personal;

import com.Gym.Dto.Personal.Mapper.PersonalMapper;
import com.Gym.Dto.Personal.PersonalAdminResponse;
import com.Gym.Dto.Personal.Auth.PersonalRegisterRequest;
import com.Gym.Model.Users_Models.Personal.Personal;
import com.Gym.Service.PersonalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/personal")
@RequiredArgsConstructor
public class PersonalController {

    private final PersonalService personalService;
    private final PersonalMapper personalMapper;



    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody PersonalRegisterRequest personalRegisterRequest){
        personalService.register(personalRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{personalId}")
    public ResponseEntity<PersonalAdminResponse> findById(@PathVariable UUID personalId){
        Personal personal =personalService.findById(personalId);
        PersonalAdminResponse personalResponse= personalMapper.EntityToAdminResponse(personal);

        return ResponseEntity.ok(personalResponse);
    }

}
