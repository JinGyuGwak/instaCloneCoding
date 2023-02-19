package com.example.demo.src.admin.service;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.admin.model.UserDetailRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.entity.BaseEntity.*;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminUserService {
    private final UserRepository userRepository;

    //관리자페이지 유저 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsers(Pageable pageable) {
        Page<User> userList= userRepository.findAll(pageable);
        List<AdminUserRequestRes> adminUserRequestResList = new ArrayList<>();
        for(User user : userList){
            AdminUserRequestRes a = new AdminUserRequestRes(user);
            adminUserRequestResList.add(a);
        }
        return adminUserRequestResList;
    }
    //관리자페이지 유저 필터(이메일,이름) 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsersFilterParam(Map<String,String> param, Pageable pageable) {
        Page<User> users;
        List<AdminUserRequestRes> adminUserRequestResList = new ArrayList<>();
        if(param.get("email")==null){
            Page<User> userList= userRepository.findAllByName(param.get("name"), pageable);
            users=userList;
        } else if(param.get("name")==null){
            Page<User> userList= userRepository.findAllByEmail(param.get("email"), pageable);
            users=userList;
        } else {
            Page<User> userList= userRepository.findAllByEmailAndName(
                    param.get("email"),
                    param.get("name"),
                    pageable);
            users=userList;
        }
        for(User user : users){
            AdminUserRequestRes a = new AdminUserRequestRes(user);
            adminUserRequestResList.add(a);
        }
        return adminUserRequestResList;
    }
    //관리자페이지 유저 필터(생성일) 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsersFilterState(State state, Pageable pageable) {
        Page<User> userList= userRepository.findByState(state,pageable);
        List<AdminUserRequestRes> adminUserRequestResList = new ArrayList<>();
        for(User user : userList){
            AdminUserRequestRes a = new AdminUserRequestRes(user);
            adminUserRequestResList.add(a);
        }
        return adminUserRequestResList;

    }
    //관리자페이지 유저 필터(생성일) 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsersFilterCreated(LocalDateTime created, Pageable pageable) {
        Page<User> userList= userRepository.findAllByCreatedAt(created,pageable);
        List<AdminUserRequestRes> adminUserRequestResList = new ArrayList<>();
        for(User user : userList){
            AdminUserRequestRes a = new AdminUserRequestRes(user);
            adminUserRequestResList.add(a);
        }
        return adminUserRequestResList;
    }

    //관리자 페이지 유저 상세조회
    public UserDetailRes getUserDetail(Long userId){
        User user = userRepository.findById(userId).
                orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));
        return new UserDetailRes(user);
    }
    public void userAdminDelete(Long userId){
        User user = userRepository.findById(userId).
                orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));
        user.adminDelete();
    }
}
