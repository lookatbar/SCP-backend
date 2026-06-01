package com.lookatbar.scp.basicdata.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    /**
     * 密码（可选，6-255字符）
     */
    @Size(min = 6, max = 255, message = "密码长度必须在6-255之间")
    private String password;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 修改者ID（可选，不传则从上下文获取）
     */
    private String modifiedBy;
}