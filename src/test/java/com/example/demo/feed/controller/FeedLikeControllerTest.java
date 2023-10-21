package com.example.demo.feed.controller;


import com.example.demo.src.feed.controller.FeedLikeController;
import com.example.demo.src.feed.dto.FeedLikeDto;
import com.example.demo.src.feed.service.FeedLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedLikeController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class FeedLikeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMApper;
    @MockBean
    private FeedLikeService feedLikeService;

    FeedLikeDto dummyFeedLikeDto(){
        return FeedLikeDto.builder()
                .feedId(1L)
                .userId(2L)
                .build();
    }

    FeedLikeDto.GetFeedLikeRes dummyRes1(){
        return FeedLikeDto.GetFeedLikeRes.builder()
                .userEmail("test1@naver.com")
                .build();
    }
    FeedLikeDto.GetFeedLikeRes dummyRes2(){
        return FeedLikeDto.GetFeedLikeRes.builder()
                .userEmail("test2@naver.com")
                .build();
    }

    @Test
    @WithMockUser
    public void userLikeFeed_test() throws Exception{
        given(feedLikeService.userLikeFeed(any())).willReturn(dummyFeedLikeDto());
        //when & then
        mockMvc.perform(post("/feed/liked")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMApper.writeValueAsString(dummyFeedLikeDto()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("postFeedLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("feedId").description("좋아요 누를 FeedId"),
                                fieldWithPath("userId").description("좋아요 누른 User의 Id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("feedId").description("좋아요 누를 FeedId"),
                                fieldWithPath("userId").description("좋아요 누른 User의 Id")
                        )));
    }

    @Test
    @WithMockUser
    public void feedLikeDelete_test() throws Exception{
        //given
        doNothing().when(feedLikeService).feedLikeDelete(any());
        //when & then
        mockMvc.perform(delete("/feed/liked")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMApper.writeValueAsString(dummyFeedLikeDto()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("deleteFeedLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("feedId").description("좋아요 누를 FeedId"),
                                fieldWithPath("userId").description("좋아요 누른 User의 Id")
                        ),
                        responseBody()));
    }
    @Test
    @WithMockUser
    public void feedLikeSearch_test() throws Exception{
        //given
        List<FeedLikeDto.GetFeedLikeRes> list = new ArrayList<>();
        list.add(dummyRes1());
        list.add(dummyRes2());
        given(feedLikeService.feedLikeSearch(anyLong())).willReturn(list);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/feed/{feedId}/liked",1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("getFeedLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feedId").description("어떤 Feed의 좋아요 User를 조회할것인가")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].userEmail").description("해당 Feed를 좋아요 누른 유저의 Email")
                        )));
    }
}
