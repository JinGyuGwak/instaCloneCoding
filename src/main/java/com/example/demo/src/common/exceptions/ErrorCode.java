package com.example.demo.src.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT(400,"잘못된 요청입니다.");


    private int status;
    private String message;
}
