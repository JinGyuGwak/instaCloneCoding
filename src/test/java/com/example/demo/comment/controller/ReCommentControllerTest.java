package com.example.demo.comment.controller;

import com.example.demo.src.comment.controller.CommentLikeController;
import com.example.demo.src.comment.controller.ReCommentController;
import com.example.demo.src.comment.dto.ReCommentDto;
import com.example.demo.src.comment.service.CommentLikeService;
import com.example.demo.src.comment.service.ReCommentService;
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
@WebMvcTest(ReCommentController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ReCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReCommentService reCommentService;

    ReCommentDto.GetReCommentRes dummyGetReCommentRes1(){
        return ReCommentDto.GetReCommentRes.builder()
                .username("카리나")
                .updateDate(LocalDateTime.now())
                .reComment("나는 신이야")
                .build();
    }
    ReCommentDto.GetReCommentRes dummyGetReCommentRes2(){
        return ReCommentDto.GetReCommentRes.builder()
                .username("고마츠나나")
                .updateDate(LocalDateTime.now())
                .reComment("는 신이야")
                .build();
    }
    ReCommentDto.ReCommentRes dummyReCommentRes(){
        return ReCommentDto.ReCommentRes.builder()
                .id(1L)
                .commentId(1L)
                .userId(3L)
                .userName("카리나")
                .reComment("나는 신이야")
                .build();
    }
    ReCommentDto dummyReCommentDto(){
        return ReCommentDto.builder()
                .reComment("나는 신이야")
                .build();
    }
    ReCommentDto.UpdateReCommentRes dummyUpdateReCommentRes(){
        return ReCommentDto.UpdateReCommentRes.builder()
                .reCommentId(1L)
                .reComment("카리나는 신이야")
                .build();
    }
    @Test
    @WithMockUser
    public void searchReComment_test() throws Exception{
        //given
        List<ReCommentDto.GetReCommentRes> list = new ArrayList<>();
        list.add(dummyGetReCommentRes1());
        list.add(dummyGetReCommentRes2());
        given(reCommentService.searchReComment(anyLong())).willReturn(list);
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/feed/recomment/{commentId}",1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("reComment/getReComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("조회 할 CommentId")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].username").description("ReComment 작성한 userName"),
                                fieldWithPath("[].updateDate").description("ReComment 작성한 시간"),
                                fieldWithPath("[].reComment").description("ReComment 내용")
                        )

                ));
    }
    @Test
    @WithMockUser
    public void createReComment_test() throws Exception{
        given(reCommentService.createReComment(anyLong(),anyString())).willReturn(dummyReCommentRes());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/feed/recomment/{commentId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyReCommentDto()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("reComment/postReComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("조회 할 CommentId")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("reComment").description("대댓글 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("대댓글 ID"),
                                fieldWithPath("commentId").description("Comment Id"),
                                fieldWithPath("userId").description("대댓글 단 UserId"),
                                fieldWithPath("userName").description("대댓글 작성한 userName"),
                                fieldWithPath("reComment").description("대댓글 내용")

                        )
                        ));
    }
    @Test
    @WithMockUser
    public void updateReComment_test() throws Exception{
        given(reCommentService.updateReComment(anyLong(),anyString())).willReturn(dummyUpdateReCommentRes());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/feed/recomment/{reCommentId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyReCommentDto()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("reComment/updateReComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reCommentId").description("수정할 대댓글 ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("reComment").description("수정할 대댓글 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("reCommentId").description("수정된 대댓글 Id"),
                                fieldWithPath("reComment").description("수정할 대댓글 내용")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void deleteReComment_test() throws Exception{
        //given
        doNothing().when(reCommentService).deleteReComment(anyLong());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/feed/recomment/{recommentId}",1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("reComment/deleteReComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("recommentId").description("수정할 대댓글 ID")
                        ),
                        responseBody()


                        ));


    }

}
