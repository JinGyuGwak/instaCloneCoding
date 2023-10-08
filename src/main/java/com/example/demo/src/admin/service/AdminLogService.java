//package com.example.demo.src.admin.service;
//
//import com.example.demo.src.admin.repository.LogEntityRepository;
//import com.example.demo.src.admin.entitiy.LogEntity;
//import com.example.demo.src.admin.dto.response.AdminLogRequestRes;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class AdminLogService {
//    private final LogEntityRepository logEntityRepository;
//
//    @Transactional(readOnly = true)
//    public List<AdminLogRequestRes> getLog(Pageable pageable, LogEntity.Domain domain) {
//        return logEntityRepository.findByDomain(domain, pageable)
//                .stream()
//                .map(AdminLogRequestRes::new)
//                .collect(Collectors.toList());
//    }
//}
