package com.example.demo.src.common.exceptions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExceptionRes {
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
