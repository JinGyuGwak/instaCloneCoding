package com.example.demo.src.admin.model;

import com.example.demo.src.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.demo.common.entity.BaseEntity.*;

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
