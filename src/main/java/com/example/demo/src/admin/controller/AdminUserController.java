package com.example.demo.src.admin.controller;


import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.admin.dto.response.AdminUserRequestRes;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private final AdminUserService adminUserService;
    /**
     * 관리자 페이지 유저 조건 검색 //(무조건 필터가 있음)
     * [GET] /admin/filter?email=&
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("/filter")
    public ResponseEntityCustom<List<AdminUserRequestRes>> getUsersFilter(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "email", required = false) String email, //email
            @RequestParam(value = "name", required = false) String name, //name
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(value = "state", required = false) BaseEntity.State state){
        try{
            LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
            LocalDateTime toDateTime = toDate != null ? toDate.atStartOfDay() : null;
            List<AdminUserRequestRes> adminUserRequestRes = adminUserService.getUsers(email,name,fromDateTime,toDateTime,state,pageable);
            return new ResponseEntityCustom<>(adminUserRequestRes);

        }catch(BaseException exception){
            return new ResponseEntityCustom<>((exception.getStatus()));
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
    public ResponseEntityCustom<UserDto.UserDetailRes> userDetail(@PathVariable Long userId){
        UserDto.UserDetailRes userDetailRes = adminUserService.getUserDetail(userId);
        return new ResponseEntityCustom<>(userDetailRes);
    }
    @ResponseBody
    @DeleteMapping("/userdetail/{userId}")
    public ResponseEntityCustom<String> userAdminDelete(@PathVariable Long userId){
        adminUserService.userAdminDelete(userId);
        String result = "삭제완료";
        return new ResponseEntityCustom<>(result);
    }


}