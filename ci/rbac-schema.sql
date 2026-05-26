-- ============================================
-- RBAC3 权限管理系统数据库表结构
-- ============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `rbac_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(64) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `email` VARCHAR(128) UNIQUE COMMENT '邮箱',
    `phone` VARCHAR(32) UNIQUE COMMENT '手机号',
    `real_name` VARCHAR(64) COMMENT '真实姓名',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 角色表
CREATE TABLE IF NOT EXISTS `rbac_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `name` VARCHAR(64) NOT NULL UNIQUE COMMENT '角色名称',
    `code` VARCHAR(64) NOT NULL UNIQUE COMMENT '角色编码',
    `description` VARCHAR(512) COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_code` (`code`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 3. 权限表
CREATE TABLE IF NOT EXISTS `rbac_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    `name` VARCHAR(64) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(128) NOT NULL UNIQUE COMMENT '权限编码',
    `type` TINYINT DEFAULT 1 COMMENT '权限类型：1-菜单，2-按钮，3-API',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
    `path` VARCHAR(255) COMMENT '权限路径（URL）',
    `method` VARCHAR(16) COMMENT 'HTTP方法（GET/POST/PUT/DELETE）',
    `description` VARCHAR(512) COMMENT '权限描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_code` (`code`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 4. 用户-角色关联表
CREATE TABLE IF NOT EXISTS `rbac_user_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 5. 角色-权限关联表
CREATE TABLE IF NOT EXISTS `rbac_role_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 6. 角色层级表（RBAC3特有 - 角色继承）
CREATE TABLE IF NOT EXISTS `rbac_role_hierarchy` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `parent_role_id` BIGINT NOT NULL COMMENT '父角色ID',
    `child_role_id` BIGINT NOT NULL COMMENT '子角色ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_parent_role` (`parent_role_id`),
    INDEX `idx_child_role` (`child_role_id`),
    UNIQUE KEY `uk_role_hierarchy` (`parent_role_id`, `child_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色层级表（角色继承）';

-- 初始化数据
INSERT INTO `rbac_role` (`name`, `code`, `description`) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限'),
('管理员', 'ADMIN', '拥有管理权限'),
('普通用户', 'USER', '拥有基础权限');

INSERT INTO `rbac_permission` (`name`, `code`, `type`, `parent_id`, `path`, `method`, `description`) VALUES
('系统管理', 'sys:manage', 1, 0, '/sys', NULL, '系统管理菜单'),
('用户管理', 'sys:user:manage', 1, 1, '/sys/users', NULL, '用户管理菜单'),
('用户查询', 'sys:user:query', 2, 2, '/api/users', 'GET', '查询用户'),
('用户创建', 'sys:user:create', 2, 2, '/api/users', 'POST', '创建用户'),
('用户更新', 'sys:user:update', 2, 2, '/api/users/{id}', 'PUT', '更新用户'),
('用户删除', 'sys:user:delete', 2, 2, '/api/users/{id}', 'DELETE', '删除用户'),
('角色管理', 'sys:role:manage', 1, 1, '/sys/roles', NULL, '角色管理菜单'),
('角色查询', 'sys:role:query', 2, 7, '/api/roles', 'GET', '查询角色'),
('角色创建', 'sys:role:create', 2, 7, '/api/roles', 'POST', '创建角色'),
('角色更新', 'sys:role:update', 2, 7, '/api/roles/{id}', 'PUT', '更新角色'),
('角色删除', 'sys:role:delete', 2, 7, '/api/roles/{id}', 'DELETE', '删除角色'),
('权限管理', 'sys:permission:manage', 1, 1, '/sys/permissions', NULL, '权限管理菜单'),
('权限查询', 'sys:permission:query', 2, 12, '/api/permissions', 'GET', '查询权限'),
('权限创建', 'sys:permission:create', 2, 12, '/api/permissions', 'POST', '创建权限'),
('权限更新', 'sys:permission:update', 2, 12, '/api/permissions/{id}', 'PUT', '更新权限'),
('权限删除', 'sys:permission:delete', 2, 12, '/api/permissions/{id}', 'DELETE', '删除权限');

-- 超级管理员拥有所有权限
INSERT INTO `rbac_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `rbac_permission`;