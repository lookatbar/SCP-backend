package com.lookatbar.scp.basicdata.domain.model.user;

import com.lookatbar.scp.basicdata.domain.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户聚合根
 * 表示系统中的用户实体，包含用户基本信息和角色关联关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户ID（UUID）
     */
    private String id;

    /**
     * 用户名，唯一标识
     */
    private String username;

    /**
     * 用户密码（加密后）
     */
    private String password;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户状态：0-禁用，1-启用
     */
    private UserStatus status;

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
     * 用户关联的角色列表
     */
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    /**
     * 为用户添加角色
     *
     * @param role 角色对象
     */
    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    /**
     * 移除用户的指定角色
     *
     * @param role 角色对象
     */
    public void removeRole(Role role) {
        if (roles != null) {
            roles.remove(role);
        }
    }

    /**
     * 检查用户是否具有指定角色
     *
     * @param roleCode 角色编码
     * @return 是否具有该角色
     */
    public boolean hasRole(String roleCode) {
        if (roles == null) {
            return false;
        }
        return roles.stream()
                .anyMatch(role -> role.getCode().equals(roleCode));
    }

    /**
     * 检查用户是否具有指定权限（通过角色继承）
     *
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public boolean hasPermission(String permissionCode) {
        if (roles == null) {
            return false;
        }
        return roles.stream()
                .anyMatch(role -> role.hasPermission(permissionCode));
    }

    /**
     * 启用用户
     */
    public void enable() {
        this.status = UserStatus.ENABLED;
    }

    /**
     * 禁用用户
     */
    public void disable() {
        this.status = UserStatus.DISABLED;
    }
}