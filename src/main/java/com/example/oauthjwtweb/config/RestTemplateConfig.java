package com.example.oauthjwtweb.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Setting_file
public class RestTemplateConfig {

    @Bean //Spring get Setting_file
    public RestTemplate restTemplate() { //RestTemplate, 스프링이 제공하는 HTTP 클라이언트
        return new RestTemplate();
    }
}
