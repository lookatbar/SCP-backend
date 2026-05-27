package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignRoleDTO;
import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试类
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserApplicationService userApplicationService;

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        testUser = UserDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440000")
                .username("testuser")
                .email("test@example.com")
                .phone("13800138000")
                .realName("Test User")
                .status(1)
                .statusDescription("正常")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(List.of())
                .build();
    }

    @Test
    @DisplayName("创建用户 - 成功")
    void createUser_Success() throws Exception {
        UserCreateDTO createDTO = UserCreateDTO.builder()
                .username("newuser")
                .password("password123")
                .email("new@example.com")
                .phone("13900139000")
                .realName("New User")
                .build();

        when(userApplicationService.createUser(any(UserCreateDTO.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
    }

    @Test
    @DisplayName("创建用户 - 用户名不能为空")
    void createUser_UsernameBlank() throws Exception {
        UserCreateDTO createDTO = UserCreateDTO.builder()
                .username("")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("创建用户 - 密码长度不足")
    void createUser_PasswordTooShort() throws Exception {
        UserCreateDTO createDTO = UserCreateDTO.builder()
                .username("newuser")
                .password("123")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("更新用户 - 成功")
    void updateUser_Success() throws Exception {
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .email("updated@example.com")
                .realName("Updated User")
                .build();

        UserDTO updatedUser = UserDTO.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .email("updated@example.com")
                .realName("Updated User")
                .status(1)
                .build();

        when(userApplicationService.updateUser(eq(testUser.getId()), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.realName").value("Updated User"));
    }

    @Test
    @DisplayName("删除用户 - 成功")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("根据ID查询用户 - 成功")
    void getUserById_Success() throws Exception {
        when(userApplicationService.getUserById(testUser.getId())).thenReturn(testUser);

        mockMvc.perform(get("/api/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @DisplayName("查询所有用户 - 成功")
    void getAllUsers_Success() throws Exception {
        UserDTO user2 = UserDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440001")
                .username("testuser2")
                .email("test2@example.com")
                .status(1)
                .build();

        when(userApplicationService.getAllUsers()).thenReturn(Arrays.asList(testUser, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].username").value("testuser2"));
    }

    @Test
    @DisplayName("分配角色 - 成功")
    void assignRoles_Success() throws Exception {
        AssignRoleDTO assignRoleDTO = AssignRoleDTO.builder()
                .roleIds(Arrays.asList("role1", "role2"))
                .build();

        mockMvc.perform(post("/api/users/{id}/roles", testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRoleDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("撤销角色 - 成功")
    void revokeRole_Success() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}/roles/{roleId}", testUser.getId(), "role1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("检查权限 - 有权限")
    void checkPermission_HasPermission() throws Exception {
        when(userApplicationService.hasPermission(testUser.getId(), "user:read")).thenReturn(true);

        mockMvc.perform(get("/api/users/{id}/permissions/check", testUser.getId())
                        .param("permissionCode", "user:read"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("检查权限 - 无权限")
    void checkPermission_NoPermission() throws Exception {
        when(userApplicationService.hasPermission(testUser.getId(), "user:delete")).thenReturn(false);

        mockMvc.perform(get("/api/users/{id}/permissions/check", testUser.getId())
                        .param("permissionCode", "user:delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}