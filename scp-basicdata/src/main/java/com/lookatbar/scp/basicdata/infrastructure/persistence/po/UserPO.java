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
@TableName("basicdata_user")
public class UserPO {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("real_name")
    private String realName;

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