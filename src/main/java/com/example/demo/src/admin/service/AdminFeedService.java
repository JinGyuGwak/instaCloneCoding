package com.example.demo.src.admin.service;


import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.model.AdminFeedRequestRes;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.admin.model.FeedDetailRes;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.model.feed.GetFeedRes;
import com.example.demo.src.feed.repository.FeedRepository;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.entity.BaseEntity.*;
import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminFeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminFeedRequestRes> getFeed(Pageable pageable) throws BaseException {
        try{
            List<Feed> feedList = feedRepository.findAllByState(ACTIVE, pageable);
            List<AdminFeedRequestRes> adminFeedRequestResList = new ArrayList<>();
            for(Feed a : feedList){
                AdminFeedRequestRes b = new AdminFeedRequestRes(a);
                adminFeedRequestResList.add(b);
            }
            return adminFeedRequestResList;
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //관리자페이지 피드 필터(작성자 이메일) 조회
    @Transactional(readOnly = true)
    public List<AdminFeedRequestRes> getFeedFilterEmail(String email, Pageable pageable) {
        User user=userRepository.findByEmail(email).
                orElseThrow(()->new BaseException(NOT_FIND_USER));

        Page<Feed> feedList= feedRepository.findByUserId(user.getId(),pageable);
        List<AdminFeedRequestRes> adminFeedRequestResList = new ArrayList<>();
        for(Feed feed : feedList){
            AdminFeedRequestRes a = new AdminFeedRequestRes(feed);
            adminFeedRequestResList.add(a);
        }
        return adminFeedRequestResList;
    }

    //관리자페이지 피드 필터(상태) 조회
    @Transactional(readOnly = true)
    public List<AdminFeedRequestRes> getFeedFilterState(State state, Pageable pageable) {
        Page<Feed> feedList= feedRepository.findByState(state,pageable);
        List<AdminFeedRequestRes> adminFeedRequestResList = new ArrayList<>();
        for(Feed feed : feedList){
            AdminFeedRequestRes a = new AdminFeedRequestRes(feed);
            adminFeedRequestResList.add(a);
        }
        return adminFeedRequestResList;
    }
    //관리자페이지 피드 필터(생성일) 조회
    @Transactional(readOnly = true)
    public List<AdminFeedRequestRes> getFeedFilterCreated(LocalDateTime created, Pageable pageable) {
        Page<Feed> feedList= feedRepository.findAllByCreatedAt(created,pageable);
        List<AdminFeedRequestRes> adminFeedRequestResList = new ArrayList<>();
        for(Feed feed : feedList){
            AdminFeedRequestRes a = new AdminFeedRequestRes(feed);
            adminFeedRequestResList.add(a);
        }
        return adminFeedRequestResList;
    }

    //관리자페이지 피드상세 조회
    @Transactional(readOnly = true)
    public FeedDetailRes getFeedDetail(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new BaseException(NOT_FIND_FEED));

        return new FeedDetailRes(feed);
    }
    public void adminDeleteFeed(Long feedId) {
        feedRepository.deleteById(feedId);
    }
}

