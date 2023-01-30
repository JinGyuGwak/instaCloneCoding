package com.example.demo.src.feed.repository;

import com.example.demo.src.feed.entity.FeedContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedContentRepository extends JpaRepository<FeedContent, Long> {
}
