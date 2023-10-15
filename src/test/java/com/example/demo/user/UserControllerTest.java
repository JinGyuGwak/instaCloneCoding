package com.example.demo.user;
import com.example.demo.src.user.controller.UserController;
import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.user.dto.UserDto.PostUserRes;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.enumeration.Role;
import com.example.demo.src.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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

    UserDto dummyUserDto(){
        return UserDto.builder()
                .email("test2695@naver.com")
                .password("test")
                .name("변경합니다")
                .build();
    }


    PostUserRes dummyPostUserRes(){
        return PostUserRes.builder()
                .email("test@naver.com")
                .password("test")
                .name("진규")
                .id(1L)
                .jwt("로그인 할 시에만 JWT 출력")
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
        given(userService.createUser(any())).willReturn(dummyPostUserRes());
        mockMvc.perform(post("/app/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMApper.writeValueAsString(dummyPostUserRes()))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(document("createUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("name").description("유저이름").type(JsonFieldType.STRING),
                                fieldWithPath("id").description("유저Id").type(JsonFieldType.NUMBER)

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
                .andDo(document("getUsersWithOutEmail",
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
                .andDo(document("getUserByEmail",
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
                        .content(objectMApper.writeValueAsString(dummyUserDto()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("modifyUserName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("유저 ID")),
                        relaxedRequestFields(
                                fieldWithPath("email").description("유저이메일").type(JsonFieldType.STRING),
                                fieldWithPath("password").description("유저비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("name").description("유저이름").type(JsonFieldType.STRING)
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
                .andDo(document("deleteUser",
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
        given(userService.logIn(anyString(),anyString())).willReturn(dummyPostUserRes());
        //when & then
        mockMvc.perform(post("/app/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMApper.writeValueAsString(dummyUserDto()))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("loginUser",
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

