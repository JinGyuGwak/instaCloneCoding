package com.example.demo.src.user.service;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.repository.LogEntityRepository;
import com.example.demo.src.admin.entitiy.LogEntity;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.dto.response.GetUserRes;
import com.example.demo.src.user.dto.response.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.src.admin.entitiy.LogEntity.Domain.USER;

// Service Create, Update, Delete 의 로직 처리
@RequiredArgsConstructor
@Service
public class UserService {

    private final FuncUser funcUser;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final LogEntityRepository logEntityRepository;


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
            encryptPwd = new SHA256().encrypt(password);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }


        User saveUser = userRepository.save(new User(email,encryptPwd,name));

        LogEntity logEntity= new LogEntity(saveUser.getEmail(), USER,"유저생성");
        logEntityRepository.save(logEntity);
        return new PostUserRes(saveUser.getId());
    }

    //유저 이름 변경
    @Transactional
    public void modifyUserName(Long userId, String name) {
        User user = funcUser.findUserByIdAndState(userId);
        user.updateName(name);

        LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저이름변경");
        logEntityRepository.save(logEntity);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = funcUser.findUserByIdAndState(userId);
        user.deleteUser();

        LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저상태삭제");
        logEntityRepository.save(logEntity);
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        return userRepository.findAllByState(ACTIVE)
                .stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        List<GetUserRes> getUserResList = new ArrayList<>();
        getUserResList.add(new GetUserRes(user));

        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = funcUser.findUserByIdAndState(userId);
        return new GetUserRes(user);
    }


    @Transactional
    public PostUserRes logIn(String email, String password) {
        //이메일에 맞는 User 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        switch (user.getState()) {
            case ACTIVE: {
                String encryptPwd;
                try { //비밀번호 검사
                    encryptPwd = new SHA256().encrypt(password);
                } catch (Exception exception) {
                    throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
                }
                if (user.getPassword().equals(encryptPwd)) {
                    Long userId = user.getId(); //유저의 기본키
                    String jwt = jwtService.createJwt(userId);
                    user.lastLogin();

                    LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저로그인");
                    logEntityRepository.save(logEntity);
                    return new PostUserRes(userId, jwt);
                } else {
                    throw new BaseException(FAILED_TO_LOGIN);
                }
            }
            case SLEEP: {
                throw new BaseException(INVALID_USER_SLEEP);
            }
            case DENIED: {
                throw new BaseException(INVALID_USER_DENIED);
            }
            case INACTIVE: {
                throw new BaseException(INVALID_USER_INACTIVE);
            }
            default:{
                throw new BaseException(NOT_FIND_USER);
            }
        }

    }

    public User searchUserById(Long id){
        return funcUser.findUserByIdAndState(id);
    }
}
