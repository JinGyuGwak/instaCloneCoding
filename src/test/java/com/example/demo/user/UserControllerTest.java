//package com.example.demo.user;
//
//import com.example.demo.src.user.controller.UserController;
//import com.example.demo.src.user.dto.UserDto;
//import com.example.demo.src.user.dto.UserDto.PostUserRes;
//import com.example.demo.src.user.entitiy.User;
//import com.example.demo.src.user.enumeration.Role;
//import com.example.demo.src.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@WebMvcTest(UserController.class)
//@SpringBootTest
//@Transactional
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMApper;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private EntityManager em;
//    @MockBean
//    private UserService userService;
//
//
//    PostUserRes dummyPostUserRes(){
//        return PostUserRes.builder()
//                .email("test@naver.com")
//                .password("test")
//                .name("진규")
//                .build();
//    }
//
//    @Test
//    @WithMockUser(username = "test")
//    public void createUser_test() throws Exception {
//        //given
//        given(userService.createUser(any())).willReturn(dummyPostUserRes());
//        mockMvc.perform(post("/app/users")
//                .contentType(MediaType.APPLICATION_JSON))
//
//
//
//
//
//    }
//}
