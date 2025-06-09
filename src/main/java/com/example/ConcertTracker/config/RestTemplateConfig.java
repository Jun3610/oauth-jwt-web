package com.example.ConcertTracker.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // 설정파일임을 알리는 어노테이션
public class RestTemplateConfig {

    @Bean //설정파일을 하나 만들어서 스프링이 가지고 있게 하는 어노테이션
    public RestTemplate restTemplate() { //RestTemplate, 스프링이 제공하는 HTTP 클라이언트
        return new RestTemplate();
    }
}
