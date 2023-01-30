package com.example.demo.src.myPage;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.myPage.model.MyPageUpdateDto;
import com.example.demo.src.myPage.model.MyPageRequestRes;
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
    public BaseResponse<MyPageRequestRes> createMyPage(@RequestBody MyPageUpdateDto myPageUpdateDto) {

        MyPageRequestRes myPageRequestRes = myPageService.createMyPage(myPageUpdateDto);
        return new BaseResponse<>(myPageRequestRes);
    }
    /**
     * 마이페이지 수정
     * [PATCH] /mypage
     * 리퀘스트바디에 userId,이름,닉네임,소개 넣기
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<MyPageRequestRes> updateMyPage(@RequestBody MyPageUpdateDto myPageUpdateDto) {
        MyPageRequestRes myPageRequestRes =
                myPageService.updateMyPage(myPageUpdateDto);
        return new BaseResponse<>(myPageRequestRes);
    }
    /**
     * 마이페이지 조회
     * [GET] /mypage/{userId}
     */
    @ResponseBody
    @GetMapping("{userId}")
    public BaseResponse<MyPageRequestRes> GetMyPage(@PathVariable Long userId) {
        MyPageRequestRes myPageRequestRes =
                myPageService.GetMyPage(userId);
        return new BaseResponse<>(myPageRequestRes);
    }

}
