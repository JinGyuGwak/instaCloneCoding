package com.example.demo.followMapping;

import com.example.demo.src.followMapping.controller.FollowController;
import com.example.demo.src.followMapping.dto.FollowMappingDto;
import com.example.demo.src.followMapping.service.FollowService;
import com.example.demo.src.user.controller.UserController;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class FollowMappingTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FollowService followService;



    FollowMappingDto.PostFollowDto dummyPostFollowDto(){
        return FollowMappingDto.PostFollowDto.builder()
                .followUserId(1L)
                .followerUserId(3L)
                .build();
    }
    FollowMappingDto.FollowDto dummyFollowDto1(){
        return FollowMappingDto.FollowDto.builder()
                .followUserId(1L)
                .loginUserEmail("test111@naver.com")
                .build();
    }
    FollowMappingDto.FollowDto dummyFollowDto2(){
        return FollowMappingDto.FollowDto.builder()
                .followUserId(2L)
                .loginUserEmail("test222@naver.com")
                .build();
    }
    List<FollowMappingDto.FollowDto> dummyFollowDtoList(){
        List<FollowMappingDto.FollowDto> list = new ArrayList<>();
        list.add(dummyFollowDto1());
        list.add(dummyFollowDto2());
        return list;
    }
    FollowMappingDto.FollowerDto dummyFollowerDto1(){
        return FollowMappingDto.FollowerDto.builder()
                .followerUserId(1L)
                .loginUserEmail("test111@naver.com")
                .build();
    }
    FollowMappingDto.FollowerDto dummyFollowerDto2(){
        return FollowMappingDto.FollowerDto.builder()
                .followerUserId(2L)
                .loginUserEmail("test222@naver.com")
                .build();
    }
    List<FollowMappingDto.FollowerDto> dummyFollowerDtoList(){
        List<FollowMappingDto.FollowerDto> list = new ArrayList<>();
        list.add(dummyFollowerDto1());
        list.add(dummyFollowerDto2());
        return list;
    }


    @Test
    @WithMockUser
    public void followUser_test() throws Exception{
        given(followService.followUser(anyLong(),anyLong())).willReturn(dummyPostFollowDto());
        mockMvc.perform(post("/follow")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyPostFollowDto()))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(document("postFollowUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("followUserId").description("팔로우 당하는 사람 Id(ex.인플루언서)").type(JsonFieldType.NUMBER),
                                fieldWithPath("followerUserId").description("팔로우 하는 사람 Id(ex.로그인한유저)").type(JsonFieldType.NUMBER)

                        ),
                        relaxedResponseFields(
                                fieldWithPath("followUserId").description("팔로우 당하는 사람 Id(ex.인플루언서)").type(JsonFieldType.NUMBER),
                                fieldWithPath("followerUserId").description("팔로우 하는 사람 Id(ex.로그인한유저)").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void followMappingSearch_test() throws Exception{
        given(followService.followSearch(anyLong())).willReturn(dummyFollowDtoList());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/follow/{userId}",1L))
                .andExpect(status().isOk())
                .andDo(document("getFollowUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("어떤 유저의 팔로우 목록을 볼것인가.")),
                        relaxedResponseFields(
                                fieldWithPath("[].followUserId").description("팔로우 목록의 userId").type(JsonFieldType.NUMBER),
                                fieldWithPath("[].loginUserEmail").description("로그인 한 유저의 email").type(JsonFieldType.STRING)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void followerSearch_test() throws Exception{
        given(followService.followerSearch(anyLong())).willReturn(dummyFollowerDtoList());
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/follow/follower/{userId}",1L))
                .andExpect(status().isOk())
                .andDo(document("getFollowerUser",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("어떤 유저의 팔로워 목록을 볼것인가.")),
                        relaxedResponseFields(
                                fieldWithPath("[].followerUserId").description("팔로워 목록의 userId").type(JsonFieldType.NUMBER),
                                fieldWithPath("[].loginUserEmail").description("로그인 한 유저의 email").type(JsonFieldType.STRING)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void followDelete_test() throws Exception{
        //given
        doNothing().when(followService).followDelete(anyLong(),anyLong());
        //when & then
        mockMvc.perform(delete("/follow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyPostFollowDto()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("deleteFollow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("followUserId").description("팔로우 당하는 사람 Id(ex.인플루언서)").type(JsonFieldType.NUMBER),
                                fieldWithPath("followerUserId").description("팔로우 하는 사람 Id(ex.로그인한유저)").type(JsonFieldType.NUMBER)

                        ),
                        responseBody()
                        ));
    }
}
