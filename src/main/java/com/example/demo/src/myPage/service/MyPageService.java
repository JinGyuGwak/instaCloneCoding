package com.example.demo.src.myPage.service;

import com.example.demo.src.util.FuncUser;
import com.example.demo.src.myPage.dto.MyPageDto;
import com.example.demo.src.myPage.repository.MyPageRepository;
import com.example.demo.src.myPage.entitiy.MyPage;
import com.example.demo.src.user.entitiy.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class MyPageService {
    private final FuncUser funcUser;
    private final MyPageRepository myPageRepository;

    //마이페이지 생성
    @Transactional
    public MyPageDto createMyPage(@NonNull MyPageDto myPageDto){
        User user = funcUser.findUserByIdAndState(myPageDto.getUserId());
        if(myPageRepository.findByUserIdAndState(myPageDto.getUserId(),ACTIVE).isPresent()){
            throw new IllegalArgumentException("이미 생성되었습니다.");
        }
        MyPage myPage=new MyPage(user, myPageDto);
        myPageRepository.save(myPage);
        return new MyPageDto(myPage);
    }
    //마이페이지 수정
    @Transactional
    public MyPageDto updateMyPage(@NonNull MyPageDto myPageDto){
        MyPage myPage=myPageRepository.findByUserIdAndState(myPageDto.getUserId(),ACTIVE)
                        .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        myPage.update(myPageDto);
        return new MyPageDto(myPage);
    }

    //마이페이지 조회
    @Transactional(readOnly = true)
    public MyPageDto getMyPage(@NonNull Long userId){
        MyPage myPage=myPageRepository.findByUserIdAndState(userId,ACTIVE)
                .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new MyPageDto(myPage);
    }
}
