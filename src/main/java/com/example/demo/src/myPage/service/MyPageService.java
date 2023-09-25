package com.example.demo.src.myPage.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.myPage.repository.MyPageRepository;
import com.example.demo.src.myPage.entitiy.MyPage;
import com.example.demo.src.myPage.dto.request.MyPageDto;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.common.entity.BaseEntity.State.*;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final FuncUser funcUser;
    private final MyPageRepository myPageRepository;

    //마이페이지 생성
    @Transactional
    public MyPageDto createMyPage(MyPageDto myPageDto){
        User user = funcUser.findUserByIdAndState(myPageDto.getUserId());
        if(myPageRepository.findByUserIdAndState(myPageDto.getUserId(),ACTIVE).isPresent()){
            throw new BaseException(INVALID_MYPAGE);
        }
        MyPage myPage=new MyPage(user, myPageDto);
        myPageRepository.save(myPage);

        return new MyPageDto(myPage);
    }
    //마이페이지 수정
    @Transactional
    public MyPageDto updateMyPage(MyPageDto myPageDto){
        MyPage myPage=myPageRepository.findByUserIdAndState(myPageDto.getUserId(),ACTIVE)
                        .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        myPage.update(myPageDto);
        return new MyPageDto(myPage);
    }

    //마이페이지 조회
    @Transactional
    public MyPageDto GetMyPage(Long userId){
        MyPage myPage=myPageRepository.findByUserIdAndState(userId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        return new MyPageDto(myPage);
    }
}
