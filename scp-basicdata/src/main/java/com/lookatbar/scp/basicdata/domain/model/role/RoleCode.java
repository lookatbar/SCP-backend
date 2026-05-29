package com.lookatbar.scp.basicdata.domain.model.role;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

/**
 * 角色编码枚举
 * 定义系统中预定义的角色类型
 * 
 * <p>角色说明：
 * <ul>
 *   <li>SUPER_ADMIN - 超级管理员：拥有系统所有权限</li>
 *   <li>NORMAL_USER - 普通用户：拥有基础操作权限</li>
 * </ul>
 */
public enum RoleCode {

    /**
     * 超级管理员
     * 拥有系统所有权限，不受权限校验限制
     */
    SUPER_ADMIN("超级管理员"),

    /**
     * 普通用户
     * 拥有基础操作权限，需根据角色配置的权限进行访问控制
     */
    NORMAL_USER("普通用户");

    /**
     * 角色名称（中文显示名）
     */
    private final String displayName;

    /**
     * 构造函数
     *
     * @param displayName 角色显示名称
     */
    RoleCode(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取角色显示名称
     *
     * @return 角色显示名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 获取角色编码（即枚举名称）
     *
     * @return 角色编码
     */
    public String getCode() {
        return this.name();
    }

    /**
     * 判断是否为超级管理员
     *
     * @return 是否为超级管理员
     */
    public boolean isSuperAdmin() {
        return this == SUPER_ADMIN;
    }

    /**
     * 判断是否为普通用户
     *
     * @return 是否为普通用户
     */
    public boolean isNormalUser() {
        return this == NORMAL_USER;
    }

    /**
     * 根据角色编码获取枚举值
     *
     * @param code 角色编码
     * @return 对应的枚举值，如果未找到返回 null
     */
    public static RoleCode fromCode(String code) {
        if (code == null) {
            return null;
        }
        try {
            return RoleCode.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 判断给定编码是否为有效的角色编码
     *
     * @param code 角色编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }


    /**
     * 是否超级管理员
     * @param roleCodes
     * @return
     */
    public static boolean isSuperAdmin(List<String> roleCodes) {
        return CollectionUtil.contains(roleCodes,SUPER_ADMIN.getCode());
    }
}
