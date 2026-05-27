package com.lookatbar.scp.basicdata.domain.model.role;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色聚合根
 * 表示系统中的角色实体，支持角色继承和权限分配，实现RBAC3权限模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

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
     * 创建人ID
     */
    private String createdBy;

    /**
     * 修改人ID
     */
    private String modifiedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 角色直接关联的权限列表
     */
    @Builder.Default
    private List<Permission> permissions = new ArrayList<>();

    /**
     * 父角色列表（角色继承）
     */
    @Builder.Default
    private List<Role> parentRoles = new ArrayList<>();

    /**
     * 子角色列表（角色继承）
     */
    @Builder.Default
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
     * @return 是否具有该权限
     */
    public boolean hasPermission(String permissionCode) {
        if (permissions == null) {
            return false;
        }
        boolean hasDirectPermission = permissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));

        if (hasDirectPermission) {
            return true;
        }

        if (parentRoles != null) {
            return parentRoles.stream()
                    .anyMatch(parentRole -> parentRole.hasPermission(permissionCode));
        }

        return false;
    }

    /**
     * 添加父角色（设置角色继承关系）
     *
     * @param parentRole 父角色对象
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
     * 添加子角色（设置角色继承关系）
     *
     * @param childRole 子角色对象
     */
    public void addChildRole(Role childRole) {
        if (childRoles == null) {
            childRoles = new ArrayList<>();
        }
        if (!childRoles.contains(childRole)) {
            childRoles.add(childRole);
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
}