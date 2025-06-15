package com.example.oauthjwtweb.dto.googleAuthDto;

import lombok.Data;

@Data
public class UserInfoRequestDto {

    private String sub;

    private String name;

    private String givenName;

    private String familyName;

    private String picture;

    private String email;

    private boolean email_verified;

    private String locale;

    private String hd;


}
