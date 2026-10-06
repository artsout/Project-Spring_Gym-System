package com.Gym.Controller;


import com.Gym.Service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal")
@RequiredArgsConstructor
public class PersonalLoginController {

    private final PersonalService personalService;


}
