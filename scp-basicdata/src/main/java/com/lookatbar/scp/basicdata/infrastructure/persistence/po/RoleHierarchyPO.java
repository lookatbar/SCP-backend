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
 * 角色继承关系持久化对象
 * 对应数据库表 basicdata_role_hierarchy
 * 用于记录角色之间的继承关系（RBAC3模型支持）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("basicdata_role_hierarchy")
public class RoleHierarchyPO {

    /**
     * 关联ID（UUID）
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父角色ID
     */
    @TableField("parent_role_id")
    private String parentRoleId;

    /**
     * 子角色ID（继承父角色的权限）
     */
    @TableField("child_role_id")
    private String childRoleId;

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