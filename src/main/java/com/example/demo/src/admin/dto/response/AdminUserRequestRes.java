package com.example.demo.src.admin.dto.response;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.user.entitiy.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminUserRequestRes {
    private String email;
    private String name;
    private LocalDateTime created;
    private State state;

    public AdminUserRequestRes(User user){
        this.email=user.getEmail();
        this.name=user.getName();
        this.created=user.getCreatedAt();
        this.state=user.getState();
    }
}
