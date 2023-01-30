package com.example.demo.src.admin;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.admin.model.UserDetailRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.entity.BaseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private final AdminUserService adminUserService;
    /**
     * 관리자 페이지 첫 화면
     * [GET] /admin
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<AdminUserRequestRes>> getUsers(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){
        List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsers(pageable);
        return new BaseResponse<>(adminUserRequestRes);
    }
    /**
     * 관리자 페이지 유저 조건 검색 //(무조건 필터가 있음)
     * [GET] /admin/filter?email=&
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("/filter")
    public BaseResponse<List<AdminUserRequestRes>> getUsersFilter(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Map<String, String> param, //email, name
            @RequestParam(value = "created", required = false) LocalDateTime created,
            @RequestParam(value = "state", required = false) State state){
        try{
            if(param.get("email")==null && param.get("name")==null && created==null){
                List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsersFilterState(state,pageable);
                return new BaseResponse<>(adminUserRequestRes);

            } else if(created==null && state==null){
                List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsersFilterParam(param,pageable);
                return new BaseResponse<>(adminUserRequestRes);

            } else if(state==null){
                List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsersFilterCreated(created,pageable);
                return new BaseResponse<>(adminUserRequestRes);
            }
            List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsers(pageable);
            return new BaseResponse<>(adminUserRequestRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저의 상세정보 (만든피드, 커멘드 단 피드, 커멘트내용, 좋아요남긴피드)
     * [GET] /admin/userdetail/{userId}
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/userdetail/{userId}")
    public BaseResponse<UserDetailRes> userDetail(@PathVariable Long userId){
        UserDetailRes userDetailRes = adminUserService.getUserDetail(userId);
        return new BaseResponse<>(userDetailRes);
    }
    @ResponseBody
    @DeleteMapping("/userdetail/{userId}")
    public BaseResponse<String> userAdminDelete(@PathVariable Long userId){
        adminUserService.userAdminDelete(userId);
        String result = "삭제완료";
        return new BaseResponse<>(result);
    }


}
