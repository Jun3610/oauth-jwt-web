package com.example.ConcertTracker.dto.kakaoAuthDto;

import lombok.Data;

@Data
public class UserInfoRequestDto {
    private Long Id;
    private Account KakaoAccount;
}
