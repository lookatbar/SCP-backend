package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.PermissionCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PermissionUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.PermissionApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 * 提供权限的增删改查、树形结构查询等REST API接口
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    /**
     * 权限应用服务
     */
    private final PermissionApplicationService permissionApplicationService;

    /**
     * 创建权限
     * 
     * @param dto 权限创建请求DTO，包含权限名称、编码、类型、路径等信息
     * @return 创建成功的权限信息
     */
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionCreateDTO dto) {
        PermissionDTO permission = permissionApplicationService.createPermission(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    /**
     * 更新权限信息
     * 
     * @param id  权限ID
     * @param dto 权限更新请求DTO，包含权限名称、描述、状态等可更新字段
     * @return 更新后的权限信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable String id, @Valid @RequestBody PermissionUpdateDTO dto) {
        PermissionDTO permission = permissionApplicationService.updatePermission(id, dto);
        return ResponseEntity.ok(permission);
    }

    /**
     * 删除权限
     * 
     * @param id 权限ID
     * @return 无内容响应（204）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable String id) {
        permissionApplicationService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID查询权限
     * 
     * @param id 权限ID
     * @return 权限详细信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable String id) {
        PermissionDTO permission = permissionApplicationService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    /**
     * 查询所有权限
     * 
     * @return 权限列表
     */
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionApplicationService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    /**
     * 按权限类型查询权限列表
     * 
     * @param type 权限类型：1-菜单权限，2-按钮权限，3-数据权限
     * @return 权限列表
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByType(@PathVariable Integer type) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByType(type);
        return ResponseEntity.ok(permissions);
    }

    /**
     * 查询角色拥有的权限列表（含继承权限）
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByRoleId(@PathVariable String roleId) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * 查询用户拥有的权限列表（含角色继承权限）
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PermissionDTO>> getPermissionsByUserId(@PathVariable String userId) {
        List<PermissionDTO> permissions = permissionApplicationService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }

    /**
     * 获取权限树形结构
     * 
     * @return 权限树形结构列表
     */
    @GetMapping("/tree")
    public ResponseEntity<List<PermissionDTO>> getPermissionTree() {
        List<PermissionDTO> tree = permissionApplicationService.getPermissionTree();
        return ResponseEntity.ok(tree);
    }
}