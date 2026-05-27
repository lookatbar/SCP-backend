package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.RoleApplicationService;
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
 * RoleController 单元测试类
 */
@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleApplicationService roleApplicationService;

    private RoleDTO testRole;

    @BeforeEach
    void setUp() {
        testRole = RoleDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440000")
                .name("admin")
                .code("ADMIN")
                .description("管理员角色")
                .status(1)
                .statusDescription("正常")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .permissions(List.of())
                .parentRoles(List.of())
                .childRoles(List.of())
                .build();
    }

    @Test
    @DisplayName("创建角色 - 成功")
    void createRole_Success() throws Exception {
        RoleCreateDTO createDTO = RoleCreateDTO.builder()
                .name("testRole")
                .code("TEST_ROLE")
                .description("测试角色")
                .build();

        when(roleApplicationService.createRole(any(RoleCreateDTO.class))).thenReturn(testRole);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testRole.getId()))
                .andExpect(jsonPath("$.name").value(testRole.getName()))
                .andExpect(jsonPath("$.code").value(testRole.getCode()));
    }

    @Test
    @DisplayName("创建角色 - 角色名称不能为空")
    void createRole_NameBlank() throws Exception {
        RoleCreateDTO createDTO = RoleCreateDTO.builder()
                .name("")
                .code("TEST_ROLE")
                .build();

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("创建角色 - 角色编码不能为空")
    void createRole_CodeBlank() throws Exception {
        RoleCreateDTO createDTO = RoleCreateDTO.builder()
                .name("testRole")
                .code("")
                .build();

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("更新角色 - 成功")
    void updateRole_Success() throws Exception {
        RoleUpdateDTO updateDTO = RoleUpdateDTO.builder()
                .name("updatedRole")
                .description("更新后的角色")
                .build();

        RoleDTO updatedRole = RoleDTO.builder()
                .id(testRole.getId())
                .name("updatedRole")
                .code(testRole.getCode())
                .description("更新后的角色")
                .status(1)
                .build();

        when(roleApplicationService.updateRole(eq(testRole.getId()), any(RoleUpdateDTO.class))).thenReturn(updatedRole);

        mockMvc.perform(put("/api/roles/{id}", testRole.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRole.getId()))
                .andExpect(jsonPath("$.name").value("updatedRole"));
    }

    @Test
    @DisplayName("删除角色 - 成功")
    void deleteRole_Success() throws Exception {
        mockMvc.perform(delete("/api/roles/{id}", testRole.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("根据ID查询角色 - 成功")
    void getRoleById_Success() throws Exception {
        when(roleApplicationService.getRoleById(testRole.getId())).thenReturn(testRole);

        mockMvc.perform(get("/api/roles/{id}", testRole.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRole.getId()))
                .andExpect(jsonPath("$.name").value(testRole.getName()))
                .andExpect(jsonPath("$.code").value(testRole.getCode()));
    }

    @Test
    @DisplayName("查询所有角色 - 成功")
    void getAllRoles_Success() throws Exception {
        RoleDTO role2 = RoleDTO.builder()
                .id("550e8400-e29b-41d4-a716-446655440001")
                .name("user")
                .code("USER")
                .status(1)
                .build();

        when(roleApplicationService.getAllRoles()).thenReturn(Arrays.asList(testRole, role2));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("admin"))
                .andExpect(jsonPath("$[1].name").value("user"));
    }

    @Test
    @DisplayName("分配权限 - 成功")
    void assignPermissions_Success() throws Exception {
        AssignPermissionDTO assignDTO = AssignPermissionDTO.builder()
                .permissionIds(Arrays.asList("perm1", "perm2"))
                .build();

        mockMvc.perform(post("/api/roles/{id}/permissions", testRole.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("撤销权限 - 成功")
    void revokePermission_Success() throws Exception {
        mockMvc.perform(delete("/api/roles/{roleId}/permissions/{permissionId}", testRole.getId(), "perm1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("角色继承 - 成功")
    void inheritRole_Success() throws Exception {
        mockMvc.perform(post("/api/roles/inherit")
                        .param("parentRoleId", "parentId")
                        .param("childRoleId", "childId"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("移除角色继承 - 成功")
    void removeInheritance_Success() throws Exception {
        mockMvc.perform(delete("/api/roles/inherit")
                        .param("parentRoleId", "parentId")
                        .param("childRoleId", "childId"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("检查角色权限 - 有权限")
    void checkPermission_HasPermission() throws Exception {
        when(roleApplicationService.hasPermission(testRole.getId(), "user:read")).thenReturn(true);

        mockMvc.perform(get("/api/roles/{id}/permissions/check", testRole.getId())
                        .param("permissionCode", "user:read"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("检查角色权限 - 无权限")
    void checkPermission_NoPermission() throws Exception {
        when(roleApplicationService.hasPermission(testRole.getId(), "user:delete")).thenReturn(false);

        mockMvc.perform(get("/api/roles/{id}/permissions/check", testRole.getId())
                        .param("permissionCode", "user:delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}