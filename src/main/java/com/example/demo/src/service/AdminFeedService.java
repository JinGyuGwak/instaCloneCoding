package com.example.demo.src.service;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.response.AdminFeedRequestRes;
import com.example.demo.src.response.FeedDetailRes;
import com.example.demo.src.entity.Feed;
import com.example.demo.src.repository.FeedRepository;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.*;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminFeedService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FuncUser funcUser;

    @Transactional(readOnly = true)
    public List<AdminFeedRequestRes> getFeed(String email, LocalDateTime fromDate, LocalDateTime toDate, State state,Pageable pageable) throws BaseException {
        try{
            return feedRepository.findByCriteria(email,fromDate,toDate,state, pageable)
                    .stream()
                    .map(AdminFeedRequestRes::new)
                    .collect(Collectors.toList());
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
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

