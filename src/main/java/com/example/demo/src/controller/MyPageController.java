package com.example.demo.src.controller;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.request.MyPageDto;
import com.example.demo.src.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    /**
     * 마이페이지 만들기
     * [POST] /mypage
     * 리퀘스트바디에 userId,이름,닉네임,소개 넣기
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<MyPageDto> createMyPage(@RequestBody MyPageDto myPageDto) {

        MyPageDto myPageRequestRes = myPageService.createMyPage(myPageDto);
        return new BaseResponse<>(myPageRequestRes);
    }
    /**
     * 마이페이지 수정
     * [PATCH] /mypage
     * 리퀘스트바디에 userId,이름,닉네임,소개 넣기
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<MyPageDto> updateMyPage(@RequestBody MyPageDto myPageDto) {
        MyPageDto myPageRequestRes =
                myPageService.updateMyPage(myPageDto);
        return new BaseResponse<>(myPageRequestRes);
    }
    /**
     * 마이페이지 조회
     * [GET] /mypage/{userId}
     */
    @ResponseBody
    @GetMapping("{userId}")
    public BaseResponse<MyPageDto> GetMyPage(@PathVariable Long userId) {
        MyPageDto myPageRequestRes =
                myPageService.GetMyPage(userId);
        return new BaseResponse<>(myPageRequestRes);
    }

}
