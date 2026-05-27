package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.RoleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 * 提供角色的增删改查、权限分配、角色继承等REST API接口
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    /**
     * 角色应用服务
     */
    private final RoleApplicationService roleApplicationService;

    /**
     * 创建角色
     * 
     * @param dto 角色创建请求DTO，包含角色名称、编码、描述等信息
     * @return 创建成功的角色信息
     */
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreateDTO dto) {
        RoleDTO role = roleApplicationService.createRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    /**
     * 更新角色信息
     * 
     * @param id  角色ID
     * @param dto 角色更新请求DTO，包含角色名称、描述、状态等可更新字段
     * @return 更新后的角色信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable String id, @Valid @RequestBody RoleUpdateDTO dto) {
        RoleDTO role = roleApplicationService.updateRole(id, dto);
        return ResponseEntity.ok(role);
    }

    /**
     * 删除角色
     * 
     * @param id 角色ID
     * @return 无内容响应（204）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleApplicationService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID查询角色
     * 
     * @param id 角色ID
     * @return 角色详细信息，包含权限列表和继承关系
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable String id) {
        RoleDTO role = roleApplicationService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleApplicationService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * 为角色分配权限
     * 
     * @param id  角色ID
     * @param dto 权限分配请求DTO，包含要分配的权限ID列表
     * @return 成功响应（200）
     */
    @PostMapping("/{id}/permissions")
    public ResponseEntity<Void> assignPermissions(@PathVariable String id, @Valid @RequestBody AssignPermissionDTO dto) {
        roleApplicationService.assignPermissions(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 撤销角色的指定权限
     * 
     * @param roleId       角色ID
     * @param permissionId 权限ID
     * @return 成功响应（200）
     */
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> revokePermission(@PathVariable String roleId, @PathVariable String permissionId) {
        roleApplicationService.revokePermission(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    /**
     * 设置角色继承关系
     * 子角色将继承父角色的所有权限
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     * @return 成功响应（200）
     */
    @PostMapping("/inherit")
    public ResponseEntity<Void> inheritRole(@RequestParam String parentRoleId, @RequestParam String childRoleId) {
        roleApplicationService.inheritRole(parentRoleId, childRoleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 移除角色继承关系
     * 
     * @param parentRoleId 父角色ID
     * @param childRoleId  子角色ID
     * @return 成功响应（200）
     */
    @DeleteMapping("/inherit")
    public ResponseEntity<Void> removeInheritance(@RequestParam String parentRoleId, @RequestParam String childRoleId) {
        roleApplicationService.removeInheritance(parentRoleId, childRoleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 检查角色是否具有指定权限（含继承权限）
     * 
     * @param id            角色ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    @GetMapping("/{id}/permissions/check")
    public ResponseEntity<Boolean> checkPermission(@PathVariable String id, @RequestParam String permissionCode) {
        boolean hasPermission = roleApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}