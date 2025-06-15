package com.example.oauthjwtweb.dto.kakaoAuthDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data

public class UserInfoRequestDto {
    private Long id;

    @JsonProperty("kakao_account")
    private Account KakaoAccount;
}
