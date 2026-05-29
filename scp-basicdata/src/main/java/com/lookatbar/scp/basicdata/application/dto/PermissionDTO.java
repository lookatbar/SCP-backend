package com.lookatbar.scp.basicdata.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {

    /**
     * 权限ID（UUID）
     */
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer type;

    /**
     * 权限类型描述
     */
    private String typeDescription;

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
     * 扩展信息（JSON格式，存储权限相关的额外配置）
     */
    private String extInfo;

    /**
     * 排序序号
     */
    private Integer sortOrder;

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
     * 子权限列表（用于构建权限树）
     */
    private List<PermissionDTO> children;
}