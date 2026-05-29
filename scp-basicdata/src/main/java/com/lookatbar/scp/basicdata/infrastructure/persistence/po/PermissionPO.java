package com.lookatbar.scp.basicdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限持久化对象
 * 对应数据库表 basicdata_permission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("basicdata_permission")
public class PermissionPO {

    /**
     * 权限ID（UUID）
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 权限名称
     */
    @TableField("name")
    private String name;

    /**
     * 权限编码（唯一）
     */
    @TableField("code")
    private String code;

    /**
     * 权限类型：1-菜单，2-按钮，3-API
     */
    @TableField("type")
    private Integer type;

    /**
     * 父权限ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 请求路径（用于API权限）
     */
    @TableField("path")
    private String path;

    /**
     * HTTP方法（GET/POST/PUT/DELETE）
     */
    @TableField("method")
    private String method;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 扩展信息（JSON格式，存储权限相关的额外配置）
     */
    @TableField("ext_info")
    private String extInfo;

    /**
     * 排序序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 修改人ID
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}