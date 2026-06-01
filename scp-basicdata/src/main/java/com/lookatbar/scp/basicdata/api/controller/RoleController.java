package com.lookatbar.scp.basicdata.api.controller;

import com.lookatbar.scp.basicdata.application.dto.AssignPermissionDTO;
import com.lookatbar.scp.basicdata.application.dto.PageResponseDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleCreateDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleQueryDTO;
import com.lookatbar.scp.basicdata.application.dto.RoleUpdateDTO;
import com.lookatbar.scp.basicdata.application.service.RoleApplicationService;
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
 * 角色管理控制器
 * 提供角色的增删改查、权限分配、角色继承等REST API接口
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的增删改查、权限分配与角色继承（RBAC3）")
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
    @Operation(summary = "创建角色", description = "创建新角色，需要提供角色名称和编码")
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
    @Operation(summary = "更新角色信息", description = "根据角色ID更新角色的名称、描述、状态等信息")
    public ResponseEntity<RoleDTO> updateRole(@Parameter(description = "角色ID") @PathVariable String id, @Valid @RequestBody RoleUpdateDTO dto) {
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
    @Operation(summary = "删除角色", description = "根据角色ID删除指定角色")
    public ResponseEntity<Void> deleteRole(@Parameter(description = "角色ID") @PathVariable String id) {
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
    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详细信息，包含权限列表和角色继承关系")
    public ResponseEntity<RoleDTO> getRoleById(@Parameter(description = "角色ID") @PathVariable String id) {
        RoleDTO role = roleApplicationService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * 查询所有角色（支持条件过滤和分页）
     * 使用统一的Query DTO接收查询参数，提高扩展性和可维护性
     * 
     * @param queryDTO 角色查询条件DTO，包含名称、编码、状态、分页等参数
     * @return 分页角色列表
     */
    @GetMapping
    @Operation(summary = "分页查询角色", description = "根据条件过滤分页查询角色列表，支持名称、编码、状态筛选")
    public ResponseEntity<PageResponseDTO<RoleDTO>> queryRoles(@ModelAttribute RoleQueryDTO queryDTO) {
        PageResponseDTO<RoleDTO> result = roleApplicationService.queryRoles(queryDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 为角色分配权限
     * 
     * @param id  角色ID
     * @param dto 权限分配请求DTO，包含要分配的权限ID列表
     * @return 成功响应（200）
     */
    @PostMapping("/{id}/permissions")
    @Operation(summary = "为角色分配权限", description = "为指定角色分配一个或多个权限")
    public ResponseEntity<Void> assignPermissions(@Parameter(description = "角色ID") @PathVariable String id, @Valid @RequestBody AssignPermissionDTO dto) {
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
    @Operation(summary = "撤销角色的指定权限", description = "从角色中移除指定的权限")
    public ResponseEntity<Void> revokePermission(@Parameter(description = "角色ID") @PathVariable String roleId, @Parameter(description = "权限ID") @PathVariable String permissionId) {
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
    @Operation(summary = "设置角色继承关系", description = "建立角色继承关系，子角色将继承父角色的所有权限（RBAC3特性）")
    public ResponseEntity<Void> inheritRole(@Parameter(description = "父角色ID") @RequestParam String parentRoleId, @Parameter(description = "子角色ID") @RequestParam String childRoleId) {
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
    @Operation(summary = "移除角色继承关系", description = "移除已建立的角色继承关系")
    public ResponseEntity<Void> removeInheritance(@Parameter(description = "父角色ID") @RequestParam String parentRoleId, @Parameter(description = "子角色ID") @RequestParam String childRoleId) {
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
    @Operation(summary = "检查角色是否具有指定权限", description = "检查角色是否具有指定的权限编码（包含继承的权限）")
    public ResponseEntity<Boolean> checkPermission(@Parameter(description = "角色ID") @PathVariable String id, @Parameter(description = "权限编码") @RequestParam String permissionCode) {
        boolean hasPermission = roleApplicationService.hasPermission(id, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
}