//package com.example.demo.src.admin.controller;
//
//import com.example.demo.src.common.response.ResponseEntity;
//import com.example.demo.src.admin.entitiy.LogEntity;
//import com.example.demo.src.admin.dto.response.AdminLogRequestRes;
//import com.example.demo.src.admin.service.AdminLogService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/admin/log")
//public class AdminLogController {
//    private final AdminLogService adminLogService;
//
//    /**
//     * 관리자 페이지 유저 로그화면
//     * [GET] /admin/report
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("/user")
//    public ResponseEntity<List<AdminLogRequestRes>> getUserLog(
//            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable pageable){
//        List<AdminLogRequestRes> adminLogRequestRes = adminLogService.getLog(pageable, LogEntity.Domain.USER);
//        return new ResponseEntity<>(adminLogRequestRes);
//    }
//
//    /**
//     * 관리자 페이지 피드 로그화면
//     * [GET] /admin/report
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("/feed")
//    public ResponseEntity<List<AdminLogRequestRes>> getFeedLog(
//            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable pageable){
//        List<AdminLogRequestRes> adminLogRequestRes = adminLogService.getLog(pageable, LogEntity.Domain.FEED);
//        return new ResponseEntity<>(adminLogRequestRes);
//    }
//    /**
//     * 관리자 페이지 신고 로그화면
//     * [GET] /admin/report
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("/report")
//    public ResponseEntity<List<AdminLogRequestRes>> getReportLog(
//            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable pageable){
//        List<AdminLogRequestRes> adminLogRequestRes = adminLogService.getLog(pageable, LogEntity.Domain.REPORT);
//        return new ResponseEntity<>(adminLogRequestRes);
//    }
//    /**
//     * 관리자 페이지 댓글 로그화면
//     * [GET] /admin/report
//     * @param
//     * @return
//     */
//    @ResponseBody
//    @GetMapping("/comment")
//    public ResponseEntity<List<AdminLogRequestRes>> getCommentLog(
//            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
//            Pageable pageable){
//        List<AdminLogRequestRes> adminLogRequestRes = adminLogService.getLog(pageable, LogEntity.Domain.COMMENT);
//        return new ResponseEntity<>(adminLogRequestRes);
//    }
//
//
//}