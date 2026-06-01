package com.lookatbar.scp.basicdata.application.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 角色查询条件DTO
 * 用于角色列表的条件过滤和分页查询
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class RoleQueryDTO extends BasePageQuery {

    /**
     * 角色名称（模糊查询）
     */
    @Parameter(description = "角色名称（模糊查询）")
    private String name;

    /**
     * 角色编码（模糊查询）
     */
    @Parameter(description = "角色编码（模糊查询）")
    private String code;

    /**
     * 状态：0-禁用，1-启用，-1或不传表示全部
     */
    @Parameter(description = "状态（-1表示全部）")
    private Integer status;
}