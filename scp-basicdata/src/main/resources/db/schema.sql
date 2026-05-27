-- ============================================
-- BasicData 模块 RBAC3 权限管理系统数据库表结构
-- 遵循 MySQL 数据库规范总则 v1.0
-- ============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `basicdata_user` (
    `id` CHAR(36) PRIMARY KEY COMMENT '用户 ID(UUID)',
    `username` VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `email` VARCHAR(128) UNIQUE COMMENT '邮箱',
    `phone` VARCHAR(32) UNIQUE COMMENT '手机号',
    `real_name` VARCHAR(64) COMMENT '真实姓名',
    `status` TINYINT(1) UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 角色表
CREATE TABLE IF NOT EXISTS `basicdata_role` (
    `id` CHAR(36) PRIMARY KEY COMMENT '角色 ID(UUID)',
    `name` VARCHAR(64) NOT NULL UNIQUE COMMENT '角色名称',
    `code` VARCHAR(64) NOT NULL UNIQUE COMMENT '角色编码',
    `description` VARCHAR(512) COMMENT '角色描述',
    `status` TINYINT(1) UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_code` (`code`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 3. 权限表
CREATE TABLE IF NOT EXISTS `basicdata_permission` (
    `id` CHAR(36) PRIMARY KEY COMMENT '权限 ID(UUID)',
    `name` VARCHAR(64) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(128) NOT NULL UNIQUE COMMENT '权限编码',
    `type` TINYINT(1) UNSIGNED DEFAULT 1 COMMENT '权限类型：1-菜单，2-按钮，3-API',
    `parent_id` CHAR(36) NULL COMMENT '父权限 ID（NULL 表示顶级权限）',
    `path` VARCHAR(255) COMMENT '权限路径（URL）',
    `method` VARCHAR(16) COMMENT 'HTTP 方法（GET/POST/PUT/DELETE）',
    `description` VARCHAR(512) COMMENT '权限描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT(1) UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_code` (`code`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 4. 用户 - 角色关联表
CREATE TABLE IF NOT EXISTS `basicdata_user_role` (
    `id` CHAR(36) PRIMARY KEY COMMENT '主键 ID(UUID)',
    `user_id` CHAR(36) NOT NULL COMMENT '用户 ID',
    `role_id` CHAR(36) NOT NULL COMMENT '角色 ID',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 5. 角色 - 权限关联表
CREATE TABLE IF NOT EXISTS `basicdata_role_permission` (
    `id` CHAR(36) PRIMARY KEY COMMENT '主键 ID(UUID)',
    `role_id` CHAR(36) NOT NULL COMMENT '角色 ID',
    `permission_id` CHAR(36) NOT NULL COMMENT '权限 ID',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 6. 角色层级表（RBAC3 特有 - 角色继承）
CREATE TABLE IF NOT EXISTS `basicdata_role_hierarchy` (
    `id` CHAR(36) PRIMARY KEY COMMENT '主键 ID(UUID)',
    `parent_role_id` CHAR(36) NOT NULL COMMENT '父角色 ID',
    `child_role_id` CHAR(36) NOT NULL COMMENT '子角色 ID',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_parent_role` (`parent_role_id`),
    INDEX `idx_child_role` (`child_role_id`),
    UNIQUE KEY `uk_role_hierarchy` (`parent_role_id`, `child_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色层级表（角色继承）';

-- 7. 角色互斥约束表（RBAC2 特性）
CREATE TABLE IF NOT EXISTS `basicdata_role_mutually_exclusive` (
    `id` CHAR(36) PRIMARY KEY COMMENT '主键 ID(UUID)',
    `role_id_1` CHAR(36) NOT NULL COMMENT '角色 ID1',
    `role_id_2` CHAR(36) NOT NULL COMMENT '角色 ID2',
    `created_by` CHAR(36) NOT NULL COMMENT '创建者 ID',
    `modified_by` CHAR(36) NOT NULL COMMENT '修改者 ID',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `updated_time` DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    INDEX `idx_role_1` (`role_id_1`),
    INDEX `idx_role_2` (`role_id_2`),
    UNIQUE KEY `uk_mutual_exclusive` (`role_id_1`, `role_id_2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色互斥约束表';
