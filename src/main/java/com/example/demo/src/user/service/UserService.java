package com.example.demo.src.user.service;



import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.common.login.jwt.JwtTokenProvider;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.user.dto.UserDto.*;
import com.example.demo.src.user.enumeration.Role;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entitiy.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.src.common.login.jwt.JwtFilter.AUTHORIZATION_HEADER;
import static com.example.demo.src.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@RequiredArgsConstructor
@Service
public class UserService {

    private final FuncUser funcUser;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;


    //POST
    @Transactional
    public PostUserRes createUser(String email, String password, String name) {
        //중복 체크
        Optional<User> checkUser = userRepository.findByEmailAndState(email, ACTIVE);
        if(checkUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        String encryptPwd;
        try { //비밀번호 암호화
            encryptPwd = passwordEncoder.encode(password);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        User saveUser = userRepository.save(new User(email,encryptPwd,name, Role.ROLE_USER));
        return PostUserRes.builder()
                .id(saveUser.getId())
                .email(saveUser.getEmail())
                .name(saveUser.getName())
                .build();
    }

    //유저 이름 변경
    @Transactional
    public void modifyUserName(Long userId, String name) {
        User user = funcUser.findUserByIdAndState(userId);
        user.updateName(name);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = funcUser.findUserByIdAndState(userId);
        user.deleteUser();
    }

    @Transactional(readOnly = true)
    public List<UserDto.GetUserRes> getUsers() {
        return userRepository.findAllByState(ACTIVE)
                .stream()
                .map(user -> GetUserRes.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        List<GetUserRes> getUserResList = new ArrayList<>();
        getUserResList.add(GetUserRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = funcUser.findUserByIdAndState(userId);
        return new GetUserRes(user);
    }


    @Transactional
    public PostUserRes logIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //이메일에 맞는 User 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String jwt = jwtTokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
//        return new PostUserRes(user.getId(),jwt);
        return PostUserRes.builder()
                .email(user.getEmail())
                .name(user.getName())
                .jwt(jwt)
                .build();
    }

    public User searchUserById(Long id){
        return funcUser.findUserByIdAndState(id);
    }
}
