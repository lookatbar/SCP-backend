package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDTO {

    /**
     * 角色名称（必填，2-64字符）
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 64, message = "角色名称长度必须在2-64之间")
    private String name;

    /**
     * 角色编码（必填，2-64字符，唯一）
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(min = 2, max = 64, message = "角色编码长度必须在2-64之间")
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建者ID（可选，不传则从上下文获取）
     */
    private String createdBy;
}