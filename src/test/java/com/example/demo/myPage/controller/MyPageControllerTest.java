package com.example.demo.myPage.controller;


import com.example.demo.src.myPage.controller.MyPageController;
import com.example.demo.src.myPage.dto.MyPageDto;
import com.example.demo.src.myPage.service.MyPageService;
import com.example.demo.src.user.controller.UserController;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MyPageController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class MyPageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMApper;
    @MockBean
    private MyPageService myPageService;

    MyPageDto dummyMyPageDto(){
        return MyPageDto.builder()
                .userId(1L)
                .name("곽진규")
                .nickname("나는문어")
                .introduction("안녕 나를 소개하지")
                .build();
    }
    @Test
    @WithMockUser
    public void create_my_page_test() throws Exception{
        given(myPageService.createMyPage(any())).willReturn(dummyMyPageDto());
        mockMvc.perform(post("/mypage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMApper.writeValueAsString(dummyMyPageDto()))
                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(document("createMyPage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").description("유저 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("name").description("유저 이름").type(JsonFieldType.STRING),
                                fieldWithPath("nickname").description("유저 닉네임").type(JsonFieldType.STRING),
                                fieldWithPath("introduction").description("유저 소개글").type(JsonFieldType.STRING)
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").description("유저 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("name").description("유저 이름").type(JsonFieldType.STRING),
                                fieldWithPath("nickname").description("유저 닉네임").type(JsonFieldType.STRING),
                                fieldWithPath("introduction").description("유저 소개글").type(JsonFieldType.STRING)
                        )
                        ));
    }
    @Test
    @WithMockUser
    public void update_my_page_test() throws Exception {
        given(myPageService.updateMyPage(any())).willReturn(dummyMyPageDto());
        mockMvc.perform(patch("/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMApper.writeValueAsString(dummyMyPageDto()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("updateMyPage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").description("마이페이지 수정할 유저 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("name").description("변경할 유저 이름 값").type(JsonFieldType.STRING),
                                fieldWithPath("nickname").description("변경할 유저 닉네임 값").type(JsonFieldType.STRING),
                                fieldWithPath("introduction").description("변경할 유저 소개글 값").type(JsonFieldType.STRING)
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").description("유저 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("name").description("변경된 유저 이름").type(JsonFieldType.STRING),
                                fieldWithPath("nickname").description("변경된 유저 닉네임").type(JsonFieldType.STRING),
                                fieldWithPath("introduction").description("변경된 유저 소개글").type(JsonFieldType.STRING)
                        )

                ));
    }
    @Test
    @WithMockUser
    public void get_my_page_test() throws Exception{
        given(myPageService.getMyPage(any())).willReturn(dummyMyPageDto());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/mypage/{userId}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("getMyPage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("마이페이지 조회 할 userId")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").description("유저 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("name").description("변경된 유저 이름").type(JsonFieldType.STRING),
                                fieldWithPath("nickname").description("변경된 유저 닉네임").type(JsonFieldType.STRING),
                                fieldWithPath("introduction").description("변경된 유저 소개글").type(JsonFieldType.STRING)
                        )
                ));
    }

}
