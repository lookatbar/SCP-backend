package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignRoleDTO;
import com.lookatbar.scp.basicdata.application.dto.UserCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.UserDTO;
import com.lookatbar.scp.basicdata.application.dto.UserUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
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
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDTO dto) {
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
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
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
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = userApplicationService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
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
    public ResponseEntity<Void> assignRoles(@PathVariable String id, @Valid @RequestBody AssignRoleDTO dto) {
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
    public ResponseEntity<Void> revokeRole(@PathVariable String userId, @PathVariable String roleId) {
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
    public ResponseEntity<Boolean> checkPermission(@PathVariable String id, @RequestParam String permissionCode) {
        boolean hasPermission = userApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}