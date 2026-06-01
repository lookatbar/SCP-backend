package com.lookatbar.scp.basicdata.domain.model.role;

import com.lookatbar.scp.basicdata.domain.model.common.BaseAuditableEntity;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色聚合根
 * 表示系统中的角色实体，支持角色继承和权限分配，实现RBAC3权限模型
 * 继承 BaseAuditableEntity 获得统一的审计字段
 */

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseAuditableEntity {

    /**
     * 角色ID（UUID）
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码，唯一标识
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态：0-禁用，1-启用
     */
    private RoleStatus status;

    /**
     * 角色直接关联的权限列表
     */
    private List<Permission> permissions = new ArrayList<>();

    /**
     * 父角色列表（角色继承）
     */
    private List<Role> parentRoles = new ArrayList<>();

    /**
     * 子角色列表（角色继承）
     */
    private List<Role> childRoles = new ArrayList<>();

    /**
     * 为角色添加权限
     *
     * @param permission 权限对象
     */
    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    /**
     * 移除角色的指定权限
     *
     * @param permission 权限对象
     */
    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    /**
     * 检查角色是否具有指定权限（含继承权限）
     *
     * @param permissionCode 权限编码
     * @return true-具有权限，false-无权限
     */
    public boolean hasPermission(String permissionCode) {
        // 检查直接权限
        if (permissions != null && permissions.stream().anyMatch(p -> p.getCode().equals(permissionCode))) {
            return true;
        }
        // 检查继承权限
        if (parentRoles != null) {
            return parentRoles.stream().anyMatch(parent -> parent.hasPermission(permissionCode));
        }
        return false;
    }

    /**
     * 获取角色所有权限（含继承权限）
     *
     * @return 权限列表
     */
    public List<Permission> getAllPermissions() {
        List<Permission> allPermissions = new ArrayList<>();
        if (permissions != null) {
            allPermissions.addAll(permissions);
        }
        if (parentRoles != null) {
            for (Role parent : parentRoles) {
                allPermissions.addAll(parent.getAllPermissions());
            }
        }
        // 去重
        return allPermissions.stream().distinct().toList();
    }

    /**
     * 添加父角色（建立角色继承关系）
     *
     * @param parentRole 父角色
     */
    public void addParentRole(Role parentRole) {
        if (parentRoles == null) {
            parentRoles = new ArrayList<>();
        }
        if (!parentRoles.contains(parentRole)) {
            parentRoles.add(parentRole);
        }
    }

    /**
     * 移除父角色
     *
     * @param parentRole 父角色
     */
    public void removeParentRole(Role parentRole) {
        if (parentRoles != null) {
            parentRoles.remove(parentRole);
        }
    }

    /**
     * 启用角色
     */
    public void enable() {
        this.status = RoleStatus.ENABLED;
    }

    /**
     * 禁用角色
     */
    public void disable() {
        this.status = RoleStatus.DISABLED;
    }

    /**
     * 检查角色是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return this.status == RoleStatus.ENABLED;
    }
}
