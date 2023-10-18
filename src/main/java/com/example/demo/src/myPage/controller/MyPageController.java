package com.example.demo.src.myPage.controller;
import com.example.demo.src.myPage.dto.MyPageDto;
import com.example.demo.src.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("")
    public ResponseEntity<MyPageDto> createMyPage(@RequestBody MyPageDto myPageDto) {
        return new ResponseEntity<>(myPageService.createMyPage(myPageDto), HttpStatus.OK);
    }
    /**
     * 마이페이지 수정
     * [PATCH] /mypage
     * 리퀘스트바디에 userId,이름,닉네임,소개 넣기
     */
    @PatchMapping("")
    public ResponseEntity<MyPageDto> updateMyPage(@RequestBody MyPageDto myPageDto) {
        return new ResponseEntity<>(myPageService.updateMyPage(myPageDto), HttpStatus.OK);
    }
    /**
     * 마이페이지 조회
     * [GET] /mypage/{userId}
     */
    @GetMapping("{userId}")
    public ResponseEntity<MyPageDto> GetMyPage(@PathVariable Long userId) {
        return new ResponseEntity<>(myPageService.getMyPage(userId), HttpStatus.OK);
    }

}
