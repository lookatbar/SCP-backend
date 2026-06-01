package com.lookatbar.scp.basicdata.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 审计字段抽象基类
 * 所有需要记录创建人、修改人、创建时间、修改时间的领域实体都应该继承此类
 * 提供统一的审计字段和访问方法
 * @NoArgsConstructor,@AllArgsConstructor,@Data 用于方便BeanUtil 进行copy属性
 * 使用 @SuperBuilder 支持子类 Builder 继承父类字段
 *
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseAuditableEntity {

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
     * 更新时间（用于乐观锁或版本控制）
     */
    private LocalDateTime updatedTime;

    /**
     * 设置创建信息
     * 在新增数据时调用，同时设置创建人和修改人
     *
     * @param userId 当前操作用户ID
     * @param now    当前时间
     */
    public void setCreateInfo(String userId, LocalDateTime now) {
        this.createdBy = userId;
        this.modifiedBy = userId;
        this.createdTime = now;
        this.modifiedTime = now;
        this.updatedTime = now;
    }

    /**
     * 设置更新信息
     * 在修改数据时调用，只设置修改人
     *
     * @param userId 当前操作用户ID
     * @param now    当前时间
     */
    public void setUpdateInfo(String userId, LocalDateTime now) {
        this.modifiedBy = userId;
        this.modifiedTime = now;
        this.updatedTime = now;
    }
}