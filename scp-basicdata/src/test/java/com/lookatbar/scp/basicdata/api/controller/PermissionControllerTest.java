package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.PermissionApplicationService;
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
 * PermissionController 单元测试类
 */
@WebMvcTest(PermissionController.class)
class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PermissionApplicationService permissionApplicationService;

    private PermissionDTO testPermission;

    @BeforeEach
    void setUp() {
        testPermission = PermissionDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440000")
                .name("用户查询")
                .code("user:read")
                .type(1)
                .typeDescription("菜单权限")
                .parentId(null)
                .path("/api/users")
                .method("GET")
                .description("查询用户列表")
                .sortOrder(1)
                .status(1)
                .statusDescription("正常")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .children(List.of())
                .build();
    }

    @Test
    @DisplayName("创建权限 - 成功")
    void createPermission_Success() throws Exception {
        PermissionCreateDTO createDTO = PermissionCreateDTO.builder()
                .name("用户查询")
                .code("user:read")
                .type(1)
                .path("/api/users")
                .method("GET")
                .description("查询用户列表")
                .build();

        when(permissionApplicationService.createPermission(any(PermissionCreateDTO.class))).thenReturn(testPermission);

        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testPermission.getId()))
                .andExpect(jsonPath("$.name").value(testPermission.getName()))
                .andExpect(jsonPath("$.code").value(testPermission.getCode()));
    }

    @Test
    @DisplayName("创建权限 - 权限名称不能为空")
    void createPermission_NameBlank() throws Exception {
        PermissionCreateDTO createDTO = PermissionCreateDTO.builder()
                .name("")
                .code("user:read")
                .type(1)
                .build();

        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("创建权限 - 权限编码不能为空")
    void createPermission_CodeBlank() throws Exception {
        PermissionCreateDTO createDTO = PermissionCreateDTO.builder()
                .name("用户查询")
                .code("")
                .type(1)
                .build();

        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("更新权限 - 成功")
    void updatePermission_Success() throws Exception {
        PermissionUpdateDTO updateDTO = PermissionUpdateDTO.builder()
                .name("用户查询更新")
                .description("更新后的描述")
                .build();

        PermissionDTO updatedPermission = PermissionDTO.builder()
                .id(testPermission.getId())
                .name("用户查询更新")
                .code(testPermission.getCode())
                .description("更新后的描述")
                .status(1)
                .build();

        when(permissionApplicationService.updatePermission(eq(testPermission.getId()), any(PermissionUpdateDTO.class))).thenReturn(updatedPermission);

        mockMvc.perform(put("/api/permissions/{id}", testPermission.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPermission.getId()))
                .andExpect(jsonPath("$.name").value("用户查询更新"));
    }

    @Test
    @DisplayName("删除权限 - 成功")
    void deletePermission_Success() throws Exception {
        mockMvc.perform(delete("/api/permissions/{id}", testPermission.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("根据ID查询权限 - 成功")
    void getPermissionById_Success() throws Exception {
        when(permissionApplicationService.getPermissionById(testPermission.getId())).thenReturn(testPermission);

        mockMvc.perform(get("/api/permissions/{id}", testPermission.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPermission.getId()))
                .andExpect(jsonPath("$.name").value(testPermission.getName()))
                .andExpect(jsonPath("$.code").value(testPermission.getCode()));
    }

    @Test
    @DisplayName("查询所有权限 - 成功")
    void getAllPermissions_Success() throws Exception {
        PermissionDTO permission2 = PermissionDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440001")
                .name("用户创建")
                .code("user:create")
                .type(1)
                .status(1)
                .build();

        when(permissionApplicationService.getAllPermissions()).thenReturn(Arrays.asList(testPermission, permission2));

        mockMvc.perform(get("/api/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("用户查询"))
                .andExpect(jsonPath("$[1].name").value("用户创建"));
    }

    @Test
    @DisplayName("按类型查询权限 - 成功")
    void getPermissionsByType_Success() throws Exception {
        when(permissionApplicationService.getPermissionsByType(1)).thenReturn(List.of(testPermission));

        mockMvc.perform(get("/api/permissions/type/{type}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("用户查询"));
    }

    @Test
    @DisplayName("按角色ID查询权限 - 成功")
    void getPermissionsByRoleId_Success() throws Exception {
        when(permissionApplicationService.getPermissionsByRoleId("role1")).thenReturn(List.of(testPermission));

        mockMvc.perform(get("/api/permissions/role/{roleId}", "role1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("user:read"));
    }

    @Test
    @DisplayName("按用户ID查询权限 - 成功")
    void getPermissionsByUserId_Success() throws Exception {
        when(permissionApplicationService.getPermissionsByUserId("user1")).thenReturn(List.of(testPermission));

        mockMvc.perform(get("/api/permissions/user/{userId}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("user:read"));
    }

    @Test
    @DisplayName("获取权限树 - 成功")
    void getPermissionTree_Success() throws Exception {
        PermissionDTO childPermission = PermissionDTO.builder()
                .id("child-id")
                .name("子权限")
                .code("user:read:detail")
                .parentId(testPermission.getId())
                .children(List.of())
                .build();

        PermissionDTO treePermission = PermissionDTO.builder()
                .id(testPermission.getId())
                .name(testPermission.getName())
                .code(testPermission.getCode())
                .children(List.of(childPermission))
                .build();

        when(permissionApplicationService.getPermissionTree()).thenReturn(List.of(treePermission));

        mockMvc.perform(get("/api/permissions/tree"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].children").isArray())
                .andExpect(jsonPath("$[0].children[0].name").value("子权限"));
    }
}