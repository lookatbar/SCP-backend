package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCreateDTO {

    @NotBlank(message = "权限名称不能为空")
    @Size(min = 2, max = 64, message = "权限名称长度必须在2-64之间")
    private String name;

    @NotBlank(message = "权限编码不能为空")
    @Size(min = 2, max = 128, message = "权限编码长度必须在2-128之间")
    private String code;

    private Integer type;

    private Long parentId;

    private String path;

    private String method;

    private String description;

    private Integer sortOrder;
}