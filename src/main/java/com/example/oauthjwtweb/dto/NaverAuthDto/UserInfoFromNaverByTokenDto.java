package com.example.oauthjwtweb.dto.naverAuthDto;

import lombok.Data;

@Data
public class UserInfoFromNaverByTokenDto {

    private String resultcode;

    private String message;

    private Response response;
}
