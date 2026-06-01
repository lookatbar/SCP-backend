package com.lookatbar.scp.basicdata.domain.model.permission;

import com.lookatbar.scp.basicdata.domain.model.common.BaseAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限实体
 * 表示系统中的权限定义，支持树形结构（父子关系）
 * 继承 BaseAuditableEntity 获得统一的审计字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseAuditableEntity {

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
     * 扩展信息（JSON格式，存储权限相关的额外配置）
     */
    private String extInfo;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    /**
     * 权限状态：0-禁用，1-启用
     */
    private PermissionStatus status;

    /**
     * 子权限列表（树形结构）
     */
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
     * 移除子权限
     *
     * @param child 子权限对象
     */
    public void removeChild(Permission child) {
        if (children != null) {
            children.remove(child);
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

    /**
     * 检查权限是否启用
     *
     * @return true-启用，false-禁用
     */
    public boolean isEnabled() {
        return this.status == PermissionStatus.ENABLED;
    }

    /**
     * 检查是否为菜单类型
     *
     * @return true-菜单类型
     */
    public boolean isMenu() {
        return this.type == PermissionType.MENU;
    }

    /**
     * 检查是否为按钮类型
     *
     * @return true-按钮类型
     */
    public boolean isButton() {
        return this.type == PermissionType.BUTTON;
    }

    /**
     * 检查是否为API类型
     *
     * @return true-API类型
     */
    public boolean isApi() {
        return this.type == PermissionType.API;
    }
}
