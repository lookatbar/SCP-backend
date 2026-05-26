package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleDTO {

    @NotEmpty(message = "角色ID列表不能为空")
    private List<Long> roleIds;
}