package com.example.demo.src.admin.service;

import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.admin.entity.LogEntity;
import com.example.demo.src.admin.model.AdminLogRequestRes;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminLogService {
    private final LogEntityRepository logEntityRepository;

    @Transactional(readOnly = true)
    public List<AdminLogRequestRes> getLog(Pageable pageable, LogEntity.Domain domain) {
        Page<LogEntity> logEntityList= logEntityRepository.findByDomain(domain,pageable);
        List<AdminLogRequestRes> adminLogRequestResList = new ArrayList<>();
        for(LogEntity logEntity : logEntityList){
            AdminLogRequestRes a = new AdminLogRequestRes(logEntity);
            adminLogRequestResList.add(a);
        }
        return adminLogRequestResList;
    }


}
