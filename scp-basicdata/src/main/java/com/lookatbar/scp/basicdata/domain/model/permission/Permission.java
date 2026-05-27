package com.lookatbar.scp.basicdata.domain.model.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限实体
 * 表示系统中的权限定义，支持树形结构（父子关系）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    /**
     * 权限ID（UUID）
     */
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码，唯一标识
     */
    private String code;

    /**
     * 权限类型：1-菜单，2-按钮，3-API
     */
    private PermissionType type;

    /**
     * 父权限ID（支持树形结构）
     */
    private String parentId;

    /**
     * 资源路径（用于API权限）
     */
    private String path;

    /**
     * HTTP方法（GET/POST/PUT/DELETE等，用于API权限）
     */
    private String method;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 权限状态：0-禁用，1-启用
     */
    private PermissionStatus status;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 修改人ID
     */
    private String modifiedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 子权限列表（树形结构）
     */
    @Builder.Default
    private List<Permission> children = new ArrayList<>();

    /**
     * 添加子权限
     *
     * @param child 子权限对象
     */
    public void addChild(Permission child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    /**
     * 启用权限
     */
    public void enable() {
        this.status = PermissionStatus.ENABLED;
    }

    /**
     * 禁用权限
     */
    public void disable() {
        this.status = PermissionStatus.DISABLED;
    }
}