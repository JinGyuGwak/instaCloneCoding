package com.example.demo.src.user.controller;


import com.example.demo.src.user.dto.UserDto.*;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;

    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */


    @PostMapping("")
    public ResponseEntity<UserDto.PostUserRes> createUser(@Valid @RequestBody UserDto postUserReq) {
        return new ResponseEntity<>(userService.createUser(
                postUserReq.getEmail(),
                postUserReq.getPassword(),
                postUserReq.getName()),HttpStatus.OK);
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /app/users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @GetMapping("")
    public ResponseEntity<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
        if(email == null){
            return new ResponseEntity<>(userService.getUsers(),HttpStatus.OK);
        }
        // TODO: 10/8/23 queryDsl 을 이용하여 메서드 하나로 처리
        // TODO: 10/8/23 이메일은 단일 조회인데 출력 타입 맞추려고 이따위로 작성
        return new ResponseEntity<>(userService.getUsersByEmail(email),HttpStatus.OK);
    }


    /**
     * 유저정보변경 API
     * name 만 수정
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody UserDto patchUserReq){
        userService.modifyUserName(userId, patchUserReq.getName());
        return new ResponseEntity<>("수정 완료!",HttpStatus.OK);

    }

    /**
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @PostMapping("/logIn")
    public ResponseEntity<PostUserRes> logIn(@RequestBody @Valid UserDto loginReq){
        return new ResponseEntity<>(userService.logIn(loginReq.getEmail(),loginReq.getPassword()),HttpStatus.OK);
    }

}
