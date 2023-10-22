package com.example.demo.comment.controller;

import com.example.demo.src.comment.controller.CommentLikeController;
import com.example.demo.src.comment.controller.FeedCommentController;
import com.example.demo.src.comment.dto.CommentLikeDto;
import com.example.demo.src.comment.service.CommentLikeService;
import com.example.demo.src.comment.service.FeedCommentService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(CommentLikeController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class CommentLikeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommentLikeService commentLikeService;

    CommentLikeDto dummyCommentLikeDto(){
        return CommentLikeDto.builder()
                .userId(1L)
                .commentId(1L)
                .build();
    }
    CommentLikeDto dummyCommentLikeDto2(){
        return CommentLikeDto.builder()
                .userId(2L)
                .commentId(1L)
                .build();
    }

    @Test
    @WithMockUser
    public void commentLike_test() throws Exception{
        given(commentLikeService.commentLike(anyLong())).willReturn(dummyCommentLikeDto());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/feed/commentLiked/{commentId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("commentLike/postCommentLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("좋아요 누를 comment 의 Id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").description("좋아요 누른 UserId"),
                                fieldWithPath("commentId").description("좋아요 눌린 commentId")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void commentLikeSearch_test() throws Exception{
        //given
        List<CommentLikeDto> list = new ArrayList<>();
        list.add(dummyCommentLikeDto());
        list.add(dummyCommentLikeDto2());
        given(commentLikeService.commentLikeSearch(anyLong())).willReturn(list);
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/feed/comment/commentLiked/{commentId}",1L))
                .andExpect(status().isOk())
                .andDo(document("commentLike/getCommentLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("좋아요 누른 User 조회할 커멘트Id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].userId").description("좋아요 누른 UserId"),
                                fieldWithPath("[].commentId").description("좋아요 눌린 commentId")

                        )

                ));
    }
    @Test
    @WithMockUser
    public void feedLikeDelete_test() throws Exception{
        //given
        doNothing().when(commentLikeService).commentLikeDelete(anyLong());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/feed/commentlike/{commentId}",1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("commentLike/deleteCommentLike",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("좋아요 누른 User 조회할 커멘트Id")
                        ),
                        responseBody()


                        ));
    }






}
