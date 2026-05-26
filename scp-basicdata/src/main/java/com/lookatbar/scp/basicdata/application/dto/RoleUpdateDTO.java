package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDTO {

    @Size(min = 2, max = 64, message = "角色名称长度必须在2-64之间")
    private String name;

    @Size(min = 2, max = 64, message = "角色编码长度必须在2-64之间")
    private String code;

    private String description;

    private Integer status;
}