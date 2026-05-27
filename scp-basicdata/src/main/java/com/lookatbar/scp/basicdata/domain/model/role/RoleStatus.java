package com.lookatbar.scp.basicdata.domain.model.role;

/**
 * 角色状态枚举
 * 定义角色的启用/禁用状态
 */
public enum RoleStatus {

    /**
     * 禁用状态
     */
    DISABLED(0, "禁用"),
    
    /**
     * 启用状态
     */
    ENABLED(1, "启用");

    /**
     * 状态编码
     */
    private final int code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param code        状态编码
     * @param description 状态描述
     */
    RoleStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取状态编码
     *
     * @return 状态编码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取状态描述
     *
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据编码获取状态枚举
     *
     * @param code 状态编码
     * @return 角色状态枚举
     * @throws IllegalArgumentException 当编码无效时抛出
     */
    public static RoleStatus fromCode(int code) {
        for (RoleStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid role status code: " + code);
    }
}