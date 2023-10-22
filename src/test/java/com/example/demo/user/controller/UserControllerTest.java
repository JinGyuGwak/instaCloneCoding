package com.example.demo.user.controller;
import com.example.demo.src.user.controller.UserController;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.user.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMApper;
    @MockBean
    private UserService userService;


    UserDto.PostUserDto dummyPostUserDto(){
        return UserDto.PostUserDto.builder()
                .email("test@naver.com")
                .password("test")
                .name("진규")
                .build();
    }
    UserDto.PostUserDto dummyPostUserResDto(){
        return UserDto.PostUserDto.builder()
                .id(1L)
                .email("test@naver.com")
                .password("test")
                .name("진규")
                .build();
    }
    UserDto.PatchUserDto dummyPatchUserDto(){
        return UserDto.PatchUserDto.builder()
                .name("변경할 이름 값")
                .build();
    }



    UserDto.LoginUserDto dummyLoginUserRequestDto(){
        return UserDto.LoginUserDto.builder()
                .email("test@naver.com")
                .password("test")
                .jwt("로그인 결과값에 출력")
                .build();
    }

    UserDto.LoginUserDto dummyLoginUserResponseDto(){
        return UserDto.LoginUserDto.builder()
                .id(1L)
                .email("test@naver.com")
                .password("test")
                .jwt("로그인 결과값에 출력")
                .build();
    }

    UserDto.GetUserRes dummyGetUserRes1(){
        return UserDto.GetUserRes.builder()
                .email("test1@naver.com")
                .name("곽진규1")
                .id(1L)
                .build();
    }
    UserDto.GetUserRes dummyGetUserRes2(){
        return UserDto.GetUserRes.builder()
                .email("test2@naver.com")
                .name("곽진규2")
                .id(2L)
                .build();
    }

    @Test
    @WithMockUser
    public void createUser_test() throws Exception {
        //given
        given(userService.createUser(any())).willReturn(dummyPostUserResDto());
        mockMvc.perform(post("/app/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMApper.writeValueAsString(dummyPostUserDto()))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(document("user/createUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("name").description("유저이름").type(JsonFieldType.STRING)

                        ),
                        relaxedResponseFields(
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("name").description("유저이름").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("유저Id").type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @WithMockUser
    public void get_user_without_email() throws Exception{
        //given
        List<UserDto.GetUserRes> getUserRes = new ArrayList<>();
        getUserRes.add(dummyGetUserRes1());
        getUserRes.add(dummyGetUserRes2());
        given(userService.getUsers()).willReturn(getUserRes);
        //when & then
        mockMvc.perform(get("/app/users"))
                .andExpect(status().isOk())
                .andDo(document("user/getUsersWithOutEmail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedResponseFields(
                                fieldWithPath("[].email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("[].name").description("유저이름").type(JsonFieldType.STRING),
                                fieldWithPath("[].id").description("유저Id").type(JsonFieldType.NUMBER)
                        )
                ));
    }

    @Test
    @WithMockUser
    public void get_user_by_email() throws Exception{
        //given
        List<UserDto.GetUserRes> getUserRes = new ArrayList<>();
        getUserRes.add(dummyGetUserRes1());
        given(userService.getUsersByEmail(anyString())).willReturn(getUserRes);
        //when & then
        mockMvc.perform(get("/app/users")
                        .param("email","test1@naver.com"))
                .andExpect(status().isOk())
                .andDo(document("user/getUserByEmail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(parameterWithName("email").description("조회할 이메일")),
                        relaxedResponseFields(
                                fieldWithPath("[].email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("[].name").description("유저이름").type(JsonFieldType.STRING),
                                fieldWithPath("[].id").description("유저Id").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void modify_user_name() throws Exception{
        //given
        doNothing().when(userService).modifyUserName(anyLong(),anyString());
        //when , then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/app/users/{userId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMApper.writeValueAsString(dummyPatchUserDto()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("user/modifyUserName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")),
                        relaxedRequestFields(
                                fieldWithPath("name").description("변경할 유저 이름 값").type(JsonFieldType.STRING)
                        ),
                        responseBody()
                        ));
    }
    @Test
    @WithMockUser
    public void delete_user() throws Exception{
        //given
        doNothing().when(userService).deleteUser(anyLong());
        //when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/app/users/{userId}",1)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("user/deleteUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")),
                        responseBody()
                        ));
    }
    @Test
    @WithMockUser
    public void login() throws Exception{
        //given
        given(userService.logIn(anyString(),anyString())).willReturn(dummyLoginUserResponseDto());
        //when & then
        mockMvc.perform(post("/app/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMApper.writeValueAsString(dummyLoginUserRequestDto()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("user/loginUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING)
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("유저ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("jwt").description("JWT 토큰").type(JsonFieldType.STRING),
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING)

                        )));

    }




}

