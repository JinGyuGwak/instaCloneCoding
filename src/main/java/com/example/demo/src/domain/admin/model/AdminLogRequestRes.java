package com.example.demo.src.domain.admin.model;

import com.example.demo.src.entity.LogEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.demo.src.entity.LogEntity.*;

@Getter
@NoArgsConstructor
public class AdminLogRequestRes {
    private Long id;
    private String email;
    private Domain domain;
    private String work;
    private LocalDateTime time;

    public AdminLogRequestRes(LogEntity logEntity){
        this.id= logEntity.getId();
        this.email= logEntity.getEmail();
        this.domain=logEntity.getDomain();
        this.work= logEntity.getWork();
        this.time=logEntity.getCreatedAt();
    }

}
