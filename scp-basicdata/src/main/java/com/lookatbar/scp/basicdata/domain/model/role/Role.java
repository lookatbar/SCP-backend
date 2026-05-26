package com.lookatbar.scp.basicdata.domain.model.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private String name;
    private String code;
    private String description;
    private RoleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<Permission> permissions = new ArrayList<>();

    @Builder.Default
    private List<Role> parentRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> childRoles = new ArrayList<>();

    public void addPermission(Permission permission) {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {
        if (permissions != null) {
            permissions.remove(permission);
        }
    }

    public boolean hasPermission(String permissionCode) {
        if (permissions == null) {
            return false;
        }
        // 检查自身权限
        boolean hasDirectPermission = permissions.stream()
                .anyMatch(permission -> permission.getCode().equals(permissionCode));

        if (hasDirectPermission) {
            return true;
        }

        // 检查继承的父角色权限（RBAC3角色继承）
        if (parentRoles != null) {
            return parentRoles.stream()
                    .anyMatch(parentRole -> parentRole.hasPermission(permissionCode));
        }

        return false;
    }

    public void addParentRole(Role parentRole) {
        if (parentRoles == null) {
            parentRoles = new ArrayList<>();
        }
        if (!parentRoles.contains(parentRole)) {
            parentRoles.add(parentRole);
        }
    }

    public void addChildRole(Role childRole) {
        if (childRoles == null) {
            childRoles = new ArrayList<>();
        }
        if (!childRoles.contains(childRole)) {
            childRoles.add(childRole);
        }
    }

    public void enable() {
        this.status = RoleStatus.ENABLED;
    }

    public void disable() {
        this.status = RoleStatus.DISABLED;
    }
}