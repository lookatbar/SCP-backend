package com.lookatbar.scp.basicdata.domain.service;

import com.lookatbar.scp.basicdata.domain.model.common.BaseAuditableEntity;
import com.lookatbar.scp.basicdata.infrastructure.context.UserContext;
import com.lookatbar.scp.basicdata.infrastructure.context.UserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 审计字段处理服务
 * 负责自动填充审计字段（创建人、修改人、创建时间、修改时间）
 * 使用 BaseAuditableEntity 作为操作对象，提供统一的审计处理
 */
@Service
public class AuditService {

    /**
     * 超级管理员默认账号（当没有上下文时使用）
     */
    private static final String DEFAULT_ADMIN_ID = "550e8400-e29b-41d4-a716-446655440001";

    /**
     * 获取当前操作用户ID
     * 优先级：手动指定 > 用户上下文 > 默认超级管理员
     *
     * @param manualUserId 手动指定的用户ID
     * @return 当前操作用户ID
     */
    public String getCurrentUserId(String manualUserId) {
        if (manualUserId != null && !manualUserId.isEmpty()) {
            return manualUserId;
        }

        UserInfo userInfo = UserContext.getUserInfo();
        if (userInfo != null && userInfo.getUserId() != null && !userInfo.getUserId().isEmpty()) {
            return userInfo.getUserId();
        }

        return DEFAULT_ADMIN_ID;
    }

    /**
     * 为新增实体设置审计字段
     *
     * @param entity       继承 BaseAuditableEntity 的实体
     * @param manualUserId 手动指定的创建者ID（可选）
     */
    public void setCreateInfo(BaseAuditableEntity entity, String manualUserId) {
        String currentUserId = getCurrentUserId(manualUserId);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateInfo(currentUserId, now);
    }

    /**
     * 为新增实体设置审计字段（不手动指定创建者）
     *
     * @param entity 继承 BaseAuditableEntity 的实体
     */
    public void setCreateInfo(BaseAuditableEntity entity) {
        setCreateInfo(entity, null);
    }

    /**
     * 为更新实体设置审计字段
     *
     * @param entity       继承 BaseAuditableEntity 的实体
     * @param manualUserId 手动指定的修改者ID（可选）
     */
    public void setUpdateInfo(BaseAuditableEntity entity, String manualUserId) {
        String currentUserId = getCurrentUserId(manualUserId);
        LocalDateTime now = LocalDateTime.now();
        entity.setUpdateInfo(currentUserId, now);
    }

    /**
     * 为更新实体设置审计字段（不手动指定修改者）
     *
     * @param entity 继承 BaseAuditableEntity 的实体
     */
    public void setUpdateInfo(BaseAuditableEntity entity) {
        setUpdateInfo(entity, null);
    }
}