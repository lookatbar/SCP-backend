package com.lookatbar.scp.basicdata.domain.model.permission;

import lombok.Getter;

/**
 * 权限类型枚举
 * 定义权限的分类：菜单、按钮、API接口
 */
@Getter
public enum PermissionType {

    /**
     * 菜单权限
     */
    MENU(1, "菜单"),
    
    /**
     * 按钮权限
     */
    BUTTON(2, "按钮"),
    
    /**
     * API接口权限
     */
    API(3, "API");

    /**
     * 类型编码
     */
    private final int code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param code        类型编码
     * @param description 类型描述
     */
    PermissionType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取类型枚举
     *
     * @param code 类型编码
     * @return 权限类型枚举
     * @throws IllegalArgumentException 当编码无效时抛出
     */
    public static PermissionType fromCode(int code) {
        for (PermissionType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid permission type code: " + code);
    }
}