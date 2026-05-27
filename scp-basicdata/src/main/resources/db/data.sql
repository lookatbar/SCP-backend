-- ============================================
-- BasicData 模块 RBAC3 权限管理系统初始化数据
-- ============================================

-- 初始化角色数据（使用 UUID）
INSERT INTO `basicdata_role` (`id`, `name`, `code`, `description`, `created_by`, `modified_by`) VALUES
(UUID(), '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 'system', 'system'),
(UUID(), '管理员', 'ADMIN', '拥有管理权限', 'system', 'system'),
(UUID(), '普通用户', 'USER', '拥有基础权限', 'system', 'system');

-- 初始化权限数据
INSERT INTO `basicdata_permission` (`id`, `name`, `code`, `type`, `parent_id`, `path`, `method`, `description`, `created_by`, `modified_by`) VALUES
(UUID(), '系统管理', 'sys:manage', 1, NULL, '/sys', NULL, '系统管理菜单', 'system', 'system'),
(UUID(), '用户管理', 'sys:user:manage', 1, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:manage') AS tmp), '/sys/users', NULL, '用户管理菜单', 'system', 'system'),
(UUID(), '用户查询', 'sys:user:query', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:user:manage') AS tmp), '/api/users', 'GET', '查询用户', 'system', 'system'),
(UUID(), '用户创建', 'sys:user:create', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:user:manage') AS tmp), '/api/users', 'POST', '创建用户', 'system', 'system'),
(UUID(), '用户更新', 'sys:user:update', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:user:manage') AS tmp), '/api/users/{id}', 'PUT', '更新用户', 'system', 'system'),
(UUID(), '用户删除', 'sys:user:delete', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:user:manage') AS tmp), '/api/users/{id}', 'DELETE', '删除用户', 'system', 'system'),
(UUID(), '角色管理', 'sys:role:manage', 1, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:manage') AS tmp), '/sys/roles', NULL, '角色管理菜单', 'system', 'system'),
(UUID(), '角色查询', 'sys:role:query', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:role:manage') AS tmp), '/api/roles', 'GET', '查询角色', 'system', 'system'),
(UUID(), '角色创建', 'sys:role:create', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:role:manage') AS tmp), '/api/roles', 'POST', '创建角色', 'system', 'system'),
(UUID(), '角色更新', 'sys:role:update', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:role:manage') AS tmp), '/api/roles/{id}', 'PUT', '更新角色', 'system', 'system'),
(UUID(), '角色删除', 'sys:role:delete', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:role:manage') AS tmp), '/api/roles/{id}', 'DELETE', '删除角色', 'system', 'system'),
(UUID(), '权限管理', 'sys:permission:manage', 1, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:manage') AS tmp), '/sys/permissions', NULL, '权限管理菜单', 'system', 'system'),
(UUID(), '权限查询', 'sys:permission:query', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:permission:manage') AS tmp), '/api/permissions', 'GET', '查询权限', 'system', 'system'),
(UUID(), '权限创建', 'sys:permission:create', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:permission:manage') AS tmp), '/api/permissions', 'POST', '创建权限', 'system', 'system'),
(UUID(), '权限更新', 'sys:permission:update', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:permission:manage') AS tmp), '/api/permissions/{id}', 'PUT', '更新权限', 'system', 'system'),
(UUID(), '权限删除', 'sys:permission:delete', 2, (SELECT id FROM (SELECT id FROM basicdata_permission WHERE code = 'sys:permission:manage') AS tmp), '/api/permissions/{id}', 'DELETE', '删除权限', 'system', 'system');
