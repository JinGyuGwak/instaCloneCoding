package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    private String email;
    private String password;
    private String name;

    private boolean isOAuth;

    public PostUserReq (User user){

    }

    public User toEntity() {
        return new User(this.email,this.password,this.name);
    }
}
