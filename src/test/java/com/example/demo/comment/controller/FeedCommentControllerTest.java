package com.example.demo.comment.controller;
import com.example.demo.src.comment.controller.FeedCommentController;
import com.example.demo.src.comment.dto.CommentDto;
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

import java.time.LocalDateTime;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedCommentController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class FeedCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FeedCommentService feedCommentService;

    CommentDto.GetFeedCommentRes dummyGetFeedCommentRes1(){
        return CommentDto.GetFeedCommentRes.builder()
                .username("test1")
                .updateDate(LocalDateTime.now())
                .commentText("댓글 테스트1")
                .build();
    }
    CommentDto.GetFeedCommentRes dummyGetFeedCommentRes2(){
        return CommentDto.GetFeedCommentRes.builder()
                .username("test2")
                .updateDate(LocalDateTime.now())
                .commentText("댓글 테스트2")
                .build();
    }
    CommentDto.FeedCommentDto dummyFeedCommentDto(){
        return CommentDto.FeedCommentDto.builder()
                .userId(1L)
                .commentText("댓글 내용")
                .build();
    }
    CommentDto.FeedCommentRes dummyFeedCommentRes(){
        return CommentDto.FeedCommentRes.builder()
                .id(1L)
                .feedId(1L)
                .userId(1L)
                .username("댓글을 단 유저이름")
                .commentText("댓글 내용")
                .build();
    }


    @Test
    @WithMockUser
    public void searchFeedComment_test() throws Exception{
        //given
        List<CommentDto.GetFeedCommentRes> list = new ArrayList<>();
        list.add(dummyGetFeedCommentRes1());
        list.add(dummyGetFeedCommentRes2());
        given(feedCommentService.getFeedComment(anyLong())).willReturn(list);

        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/feed/comment/{feedId}",1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("getFeedComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feedId").description("댓글 조회할 FeedId")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].username").description("댓글을 단 유저 이름"),
                                fieldWithPath("[].updateDate").description("댓글을 단 시간"),
                                fieldWithPath("[].commentText").description("댓글 내용")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void createComment_test() throws Exception{
        given(feedCommentService.createFeedComment(any(),anyLong())).willReturn(dummyFeedCommentRes());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/feed/{feedId}/comment",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyFeedCommentDto()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("createComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feedId").description("댓글을 달 FeedId")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("userId").description("댓글을 단 UserId"),
                                fieldWithPath("commentText").description("댓글 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("FeedCommentId"),
                                fieldWithPath("feedId").description("댓글을 달 FeedId"),
                                fieldWithPath("userId").description("댓글을 단 UserId"),
                                fieldWithPath("username").description("댓글을 단 Username"),
                                fieldWithPath("commentText").description("댓글 내용")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void updateComment_test() throws Exception{
        CommentDto.UpdateFeedCommentRes dummyResDto =
                CommentDto.UpdateFeedCommentRes.builder()
                        .commentText("카리나는 신이야")
                        .commentId(1L)
                        .build();

        CommentDto.FeedCommentUpdateRequestDto dummyReqDto=
                CommentDto.FeedCommentUpdateRequestDto.builder()
                        .commentText("카리나는 신이야")
                        .build();
        given(feedCommentService.updateFeedComment(anyLong(),anyString())).willReturn(dummyResDto);
        //when &then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/feed/comment/{commentId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyReqDto)) //객체를 문자스트림으로(JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("updateCommentDocs",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("수정할 commentId")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("commentText").description("변경할 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commentText").description("변경된 내용"),
                                fieldWithPath("commentId").description("변경된 댓글 Id")

                        )
                        ));
    }
    @Test
    @WithMockUser
    public void deleteComment_test() throws Exception{
        //given
        doNothing().when(feedCommentService).deleteFeedComment(anyLong());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/feed/comment/{commentId}",1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("deleteComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("삭제할 commentId")
                        ),
                        responseBody()

                ));
    }

}
