package com.example.oauthjwtweb.dto.googleAuthDto;

import lombok.Data;

@Data
public class AccessTokenResponeDtoFromGoogle {

    private String access_token;

    private Integer expires_in;

    private String refresh_token;

    private String token_type;

    private String scope;
}
