package com.example.demo.src.domain.myPage.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageUpdateDto {
    private Long userId;
    private String name;
    private String nickname;
    private String introduction;


}
