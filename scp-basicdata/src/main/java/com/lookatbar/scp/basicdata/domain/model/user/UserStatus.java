package com.lookatbar.scp.basicdata.domain.model.user;

/**
 * 用户状态枚举
 * 定义用户的启用/禁用状态
 */
public enum UserStatus {

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
    UserStatus(int code, String description) {
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
     * @return 用户状态枚举
     * @throws IllegalArgumentException 当编码无效时抛出
     */
    public static UserStatus fromCode(int code) {
        for (UserStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid user status code: " + code);
    }
}