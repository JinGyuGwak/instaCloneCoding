package com.example.demo.src.admin;

import com.example.demo.src.admin.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.demo.src.admin.entity.LogEntity.*;

public interface LogEntityRepository extends JpaRepository<LogEntity,Long> {


    Page<LogEntity> findByDomain(LogEntity.Domain domain, Pageable pageable);
}
