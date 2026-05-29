package com.lookatbar.scp.basicdata.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * 用户ID（UUID）
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

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
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 用户角色列表（字符串数组，前端期望格式）
     */
    private List<String> roles;

    /**
     * 用户角色详细信息列表
     */
    @JsonProperty("roleList")
    private List<RoleDTO> roleList;

    /**
     * 用户权限列表（前端期望字段名 permissions）
     */
    @JsonProperty("permissions")
    private List<String> permissions;
}