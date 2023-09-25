package com.example.demo.src.admin.repository;

import com.example.demo.src.admin.entitiy.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntityRepository extends JpaRepository<LogEntity,Long> {


    Page<LogEntity> findByDomain(LogEntity.Domain domain, Pageable pageable);
}
