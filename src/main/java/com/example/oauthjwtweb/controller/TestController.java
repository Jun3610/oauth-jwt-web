package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.dto.testDto.TestEnviornmnetVariableDto;
import com.example.oauthjwtweb.service.TesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    TesService tesService;
    TestController (TesService tesService){this.tesService = tesService;}

    @GetMapping("/environmnet")
    public TestEnviornmnetVariableDto testVariable() {
        return tesService.testEnviornmnetvarialbe();
    }
}
