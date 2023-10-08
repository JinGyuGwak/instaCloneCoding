package com.example.demo.src.admin.service;


import com.example.demo.src.admin.dto.response.AdminUserRequestRes;
import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final FuncUser funcUser;

    //관리자페이지 유저 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsers(String email, String name, LocalDateTime fromDateTime,
                                              LocalDateTime toDateTime, State state, Pageable pageable) {
        return userRepository.findByCriteria(email,name,fromDateTime,toDateTime,state,pageable)
                .stream()
                .map(AdminUserRequestRes::new)
                .collect(Collectors.toList());
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
        return userRepository.findByState(state, pageable)
                .stream()
                .map(AdminUserRequestRes::new)
                .collect(Collectors.toList());

    }
    //관리자페이지 유저 필터(생성일) 조회
    @Transactional(readOnly = true)
    public List<AdminUserRequestRes> getUsersFilterCreated(LocalDateTime created, Pageable pageable) {
        return userRepository.findAllByCreatedAt(created, pageable)
                .stream()
                .map(AdminUserRequestRes::new)
                .collect(Collectors.toList());

    }

    //관리자 페이지 유저 상세조회
    @Transactional
    public UserDto.UserDetailRes getUserDetail(Long userId){
        User user = funcUser.findUserById(userId);
        return UserDto.UserDetailRes.builder()
                .user(user)
                .build();
    }


    @Transactional
    public void userAdminDelete(Long userId){
        User user = funcUser.findUserById(userId);
        user.adminDelete();
    }
}
