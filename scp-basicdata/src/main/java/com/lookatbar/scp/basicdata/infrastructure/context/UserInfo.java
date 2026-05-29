package com.lookatbar.scp.basicdata.infrastructure.context;

import cn.hutool.core.collection.CollectionUtil;
import com.lookatbar.scp.basicdata.domain.model.role.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户上下文信息
 * 存储当前登录用户的基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户角色列表
     */
    private java.util.List<String> roles;

    /**
     * 用户权限列表
     */
    private java.util.List<String> permissions;


    /**
     * 判断是否超级管理员
     * @return
     */
    public boolean isSuperAdmin() {
        return CollectionUtil.isNotEmpty(this.roles) && this.roles.contains(RoleCode.SUPER_ADMIN.getCode());
    }
}