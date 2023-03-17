package com.example.demo.src.repository;

import com.example.demo.src.entity.FeedContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedContentRepository extends JpaRepository<FeedContent, Long> {
}
