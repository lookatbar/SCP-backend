package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色继承关系请求DTO（RBAC3模型支持）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInheritanceDTO {

    /**
     * 父角色ID（必填）
     */
    @NotBlank(message = "父角色ID不能为空")
    private String parentRoleId;

    /**
     * 子角色ID（必填，继承父角色的权限）
     */
    @NotBlank(message = "子角色ID不能为空")
    private String childRoleId;
}