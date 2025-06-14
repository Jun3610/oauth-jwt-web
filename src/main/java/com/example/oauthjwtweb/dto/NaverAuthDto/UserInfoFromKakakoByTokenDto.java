package com.example.oauthjwtweb.dto.NaverAuthDto;

import lombok.Data;

@Data
public class UserInfoFromKakakoByTokenDto {

    private String resultcode;

    private String message;

    private Response response;
}
