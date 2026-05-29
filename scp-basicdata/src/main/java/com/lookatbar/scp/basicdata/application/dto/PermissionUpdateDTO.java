package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateDTO {

    /**
     * 权限名称（2-64字符）
     */
    @Size(min = 2, max = 64, message = "权限名称长度必须在2-64之间")
    private String name;

    /**
     * 权限编码（2-128字符，唯一）
     */
    @Size(min = 2, max = 128, message = "权限编码长度必须在2-128之间")
    private String code;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer type;

    /**
     * 父权限ID
     */
    private String parentId;

    /**
     * 访问路径（URL）
     */
    private String path;

    /**
     * HTTP方法（GET/POST/PUT/DELETE等）
     */
    private String method;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 扩展信息（JSON格式，存储权限相关的额外配置，最大1024字符）
     */
    @Size(max = 1024, message = "扩展信息长度不能超过1024字符")
    private String extInfo;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}