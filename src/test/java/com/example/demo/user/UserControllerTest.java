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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMApper;
    @MockBean
    private UserService userService;


    PostUserRes dummyPostUserRes(){
        return PostUserRes.builder()
                .email("test@naver.com")
                .password("test")
                .name("진규")
                .id(1L)
                .jwt("로그인 할 시에만 JWT 출력")
                .build();
    }

    @Test
    @WithMockUser(username = "test")
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
                        )));
    }

}

