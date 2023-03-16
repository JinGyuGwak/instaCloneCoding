package com.example.demo.src.myPage;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.myPage.entity.MyPage;
import com.example.demo.src.myPage.model.MyPageUpdateDto;
import com.example.demo.src.myPage.model.MyPageRequestRes;
import com.example.demo.src.user.entity.User;
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
    public MyPageRequestRes createMyPage(MyPageUpdateDto myPageUpdateDto){
        User user = funcUser.findUserByIdAndState(myPageUpdateDto.getUserId());
        if(myPageRepository.findByUserIdAndState(myPageUpdateDto.getUserId(),ACTIVE).isPresent()){
            throw new BaseException(INVALID_MYPAGE);
        }
        MyPage myPage=new MyPage(user, myPageUpdateDto);
        myPageRepository.save(myPage);

        return new MyPageRequestRes(myPage);
    }
    //마이페이지 수정
    public MyPageRequestRes updateMyPage(MyPageUpdateDto myPageUpdateDto){
        MyPage myPage=myPageRepository.findByUserIdAndState(myPageUpdateDto.getUserId(),ACTIVE)
                        .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        myPage.update(myPageUpdateDto);
        return new MyPageRequestRes(myPage);
    }

    //마이페이지 조회
    public MyPageRequestRes GetMyPage(Long userId){
        MyPage myPage=myPageRepository.findByUserIdAndState(userId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        return new MyPageRequestRes(myPage);
    }
}
