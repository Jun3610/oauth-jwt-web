package com.example.oauthjwtweb.dto.KakaoAuthDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class UserInfoRequestDto {
    private Long id;

    @JsonProperty("kakao_account")
    private Account KakaoAccount;
}
