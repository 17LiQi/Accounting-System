package com.as.server.controller;

import com.as.server.dto.users.UserDTO;
import com.as.server.dto.users.UserRequest;
import com.as.server.entity.User;
import com.as.server.enums.Role;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EntityMapper entityMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_success() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole(Role.valueOf("USER"));

        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setIsAdmin(false);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");
        userDTO.setRole(Role.valueOf("USER"));

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(entityMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenReturn(user);
        when(entityMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_duplicateUsername_throwsBadRequest() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole(Role.valueOf("USER"));

        when(entityMapper.toUser(any(UserRequest.class))).thenReturn(new User());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.create(any(User.class))).thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void createUser_accessDenied_throwsForbidden() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole(Role.valueOf("USER"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Access is denied"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listUsers_success() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setIsAdmin(false);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");
        userDTO.setRole(Role.valueOf("USER"));

        List<User> users = Collections.singletonList(user);
        when(userService.findAll()).thenReturn(users);
        when(entityMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].role").value("USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_success() throws Exception {
        UserRequest request = new UserRequest();
        request.setUsername("updateduser");
        request.setPassword("newpassword");
        request.setRole(Role.valueOf("USER"));

        User user = new User();
        user.setUserId(1);
        user.setUsername("updateduser");
        user.setPassword("encodedNewPassword");
        user.setIsAdmin(false);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("updateduser");
        userDTO.setRole(Role.valueOf("USER"));

        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(entityMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(userService.update(eq(1), any(User.class))).thenReturn(user);
        when(entityMapper.toUserDTO(any(User.class))).thenReturn(userDTO);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_success() throws Exception {
        when(userService.hasTransactions(1)).thenReturn(false);
        doNothing().when(userService).delete(1);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_hasTransactions_throwsConflict() throws Exception {
        when(userService.hasTransactions(1)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("User has associated transactions"));
    }
}