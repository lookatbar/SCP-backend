package com.lookatbar.scp.basicdata.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("basicdata_permission")
public class PermissionPO {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("type")
    private Integer type;

    @TableField("parent_id")
    private String parentId;

    @TableField("path")
    private String path;

    @TableField("method")
    private String method;

    @TableField("description")
    private String description;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("status")
    private Integer status;

    @TableField("created_by")
    private String createdBy;

    @TableField("modified_by")
    private String modifiedBy;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

}