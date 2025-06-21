package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.dto.testDto.TestEnviornmnetVariableDto;
import com.example.oauthjwtweb.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    TestService tesService;
    TestController (TestService tesService){this.tesService = tesService;}

    @GetMapping("/environmnet")
    public TestEnviornmnetVariableDto testVariable() {
        return tesService.testEnviornmnetvarialbe();
    }
}
