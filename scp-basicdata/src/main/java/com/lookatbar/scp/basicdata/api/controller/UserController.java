package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignRoleDTO;
import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户的增删改查、角色分配与权限检查等REST API接口
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户的增删改查、角色分配与权限检查")
public class UserController {

    /**
     * 用户应用服务
     */
    private final UserApplicationService userApplicationService;

    /**
     * 创建用户
     * 
     * @param dto 用户创建请求DTO，包含用户名、密码、邮箱等信息
     * @return 创建成功的用户信息
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户，需要提供用户名、密码等基本信息")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO user = userApplicationService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * 更新用户信息
     * 
     * @param id  用户ID
     * @param dto 用户更新请求DTO，包含邮箱、电话、真实姓名等可更新字段
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户的邮箱、电话、真实姓名等信息")
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "用户ID") @PathVariable String id, @Valid @RequestBody UserUpdateDTO dto) {
        UserDTO user = userApplicationService.updateUser(id, dto);
        return ResponseEntity.ok(user);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 无内容响应（204）
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除指定用户")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable String id) {
        userApplicationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户详细信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息，包含角色关联信息")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "用户ID") @PathVariable String id) {
        UserDTO user = userApplicationService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    @Operation(summary = "查询所有用户", description = "获取系统中所有用户列表")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userApplicationService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 为用户分配角色
     * 
     * @param id  用户ID
     * @param dto 角色分配请求DTO，包含要分配的角色ID列表
     * @return 成功响应（200）
     */
    @PostMapping("/{id}/roles")
    @Operation(summary = "为用户分配角色", description = "为指定用户分配一个或多个角色")
    public ResponseEntity<Void> assignRoles(@Parameter(description = "用户ID") @PathVariable String id, @Valid @RequestBody AssignRoleDTO dto) {
        userApplicationService.assignRoles(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 撤销用户的指定角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 成功响应（200）
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "撤销用户的指定角色", description = "从用户身上移除指定的角色")
    public ResponseEntity<Void> revokeRole(@Parameter(description = "用户ID") @PathVariable String userId, @Parameter(description = "角色ID") @PathVariable String roleId) {
        userApplicationService.revokeRole(userId, roleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 检查用户是否具有指定权限
     * 
     * @param id            用户ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    @GetMapping("/{id}/permissions/check")
    @Operation(summary = "检查用户是否具有指定权限", description = "检查用户是否具有指定的权限编码（包含继承的角色权限）")
    public ResponseEntity<Boolean> checkPermission(@Parameter(description = "用户ID") @PathVariable String id, @Parameter(description = "权限编码") @RequestParam String permissionCode) {
        boolean hasPermission = userApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}