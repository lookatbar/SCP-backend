package com.lookatbar.scp.basicdata.domain.model.user;

import com.lookatbar.scp.basicdata.domain.model.common.BaseAuditableEntity;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户聚合根
 * 表示系统中的用户实体，包含用户基本信息和角色关联关系
 * 继承 BaseAuditableEntity 获得统一的审计字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseAuditableEntity {

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
     * 用户关联的角色列表
     */
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
     * 为用户移除角色
     *
     * @param role 角色对象
     */
    public void removeRole(Role role) {
        if (roles != null) {
            roles.remove(role);
        }
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

    /**
     * 检查用户是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return this.status == UserStatus.ENABLED;
    }
}
