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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rbac_role_hierarchy")
public class RoleHierarchyPO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("parent_role_id")
    private Long parentRoleId;

    @TableField("child_role_id")
    private Long childRoleId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}