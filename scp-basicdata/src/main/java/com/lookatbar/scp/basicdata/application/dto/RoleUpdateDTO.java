package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDTO {

    /**
     * 角色名称（2-64字符）
     */
    @Size(min = 2, max = 64, message = "角色名称长度必须在2-64之间")
    private String name;

    /**
     * 角色编码（2-64字符，唯一）
     */
    @Size(min = 2, max = 64, message = "角色编码长度必须在2-64之间")
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}