package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleInheritanceDTO {

    @NotNull(message = "父角色ID不能为空")
    private Long parentRoleId;

    @NotNull(message = "子角色ID不能为空")
    private Long childRoleId;
}