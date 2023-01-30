package com.example.demo.src.user;



import com.example.demo.common.entity.BaseEntity.State;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.admin.entity.LogEntity;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.src.admin.entity.LogEntity.Domain.REPORT;
import static com.example.demo.src.admin.entity.LogEntity.Domain.USER;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final LogEntityRepository logEntityRepository;


    //POST
    public PostUserRes createUser(PostUserReq postUserReq) {
        //중복 체크
        Optional<User> checkUser = userRepository.findByEmailAndState(postUserReq.getEmail(), ACTIVE);
        if(checkUser.isPresent() == true){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String encryptPwd;
        try { //비밀번호 암호화
            encryptPwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());

        LogEntity logEntity= new LogEntity(saveUser.getEmail(), USER,"유저생성");
        logEntityRepository.save(logEntity);
        return new PostUserRes(saveUser.getId());

    }

    public PostUserRes createOAuthUser(User user) {
        User saveUser = userRepository.save(user);

        // JWT 발급
        String jwtToken = jwtService.createJwt(saveUser.getId());
        return new PostUserRes(saveUser.getId(), jwtToken);

    }

    //유저 이름 변경
    public void modifyUserName(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updateName(patchUserReq.getName());

        LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저이름변경");
        logEntityRepository.save(logEntity);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();

        LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저상태삭제");
        logEntityRepository.save(logEntity);
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        List<GetUserRes> getUserResList = userRepository.findAllByEmailAndState(email, ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByEmail(String email) {
        Optional<User> result = userRepository.findByEmailAndState(email, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        //이메일에 맞는 User 찾기
        User user = userRepository.findByEmail(postLoginReq.getEmail())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        switch (user.getState()) {
            case ACTIVE: {
                String encryptPwd;
                try { //비밀번호 검사
                    encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
                } catch (Exception exception) {
                    throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
                }
                if (user.getPassword().equals(encryptPwd)) {
                    Long userId = user.getId(); //유저의 기본키
                    String jwt = jwtService.createJwt(userId);
                    String sns = user.getWhatSns();
                    user.lastLogin();

                    LogEntity logEntity= new LogEntity(user.getEmail(), USER,"유저로그인");
                    logEntityRepository.save(logEntity);
                    return new PostLoginRes(userId, sns, jwt);
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

    public GetUserRes getUserByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    public User searchUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return user;
    }
}
