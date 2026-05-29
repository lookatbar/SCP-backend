-- ========================================================
-- 权限点初始化SQL（从前端路由配置 router.config.js 提取）
-- 表结构遵循 schema.sql 定义
-- ========================================================

-- 系统管理员用户ID（用于 created_by/modified_by）
SET @ADMIN_USER_ID = '00000000-0000-0000-0000-000000000001';

-- ========================================================
-- 1. 插入菜单级权限（type=1）
-- ========================================================
INSERT INTO basicdata_permission (
    id, name, code, type, parent_id, path, description, sort_order, status,
    created_by, modified_by, created_time, modified_time, updated_time
) VALUES
-- 仪表盘
('550e8400-e29b-41d4-a716-446655440001', '仪表盘', 'dashboard', 1, NULL, '/dashboard', '仪表盘菜单权限', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 表单
('550e8400-e29b-41d4-a716-446655440002', '表单', 'form', 1, NULL, '/form', '表单菜单权限', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 列表
('550e8400-e29b-41d4-a716-446655440003', '列表', 'table', 1, NULL, '/list', '列表菜单权限', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 个人中心
('550e8400-e29b-41d4-a716-446655440004', '个人中心', 'profile', 1, NULL, '/profile', '个人中心菜单权限', 4, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 结果页
('550e8400-e29b-41d4-a716-446655440005', '结果页', 'result', 1, NULL, '/result', '结果页菜单权限', 5, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 异常页
('550e8400-e29b-41d4-a716-446655440006', '异常页', 'exception', 1, NULL, '/exception', '异常页菜单权限', 6, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 用户管理
('550e8400-e29b-41d4-a716-446655440007', '用户管理', 'user', 1, NULL, '/account', '用户管理菜单权限', 7, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 支持
('550e8400-e29b-41d4-a716-446655440008', '支持', 'support', 1, NULL, '/other', '支持菜单权限', 8, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW())

ON DUPLICATE KEY UPDATE 
    name = VALUES(name), 
    description = VALUES(description),
    sort_order = VALUES(sort_order),
    status = VALUES(status),
    modified_by = VALUES(modified_by),
    modified_time = NOW(),
    updated_time = NOW();

-- ========================================================
-- 2. 插入页面级权限（type=2）
-- ========================================================

-- 仪表盘子权限
INSERT INTO basicdata_permission (
    id, name, code, type, parent_id, path, description, sort_order, status,
    created_by, modified_by, created_time, modified_time, updated_time
) VALUES
-- 仪表盘子权限
('550e8400-e29b-41d4-a716-446655440101', '分析页', 'dashboard:analysis', 2, '550e8400-e29b-41d4-a716-446655440001', '/dashboard/analysis', '仪表盘分析页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440102', '工作台', 'dashboard:workplace', 2, '550e8400-e29b-41d4-a716-446655440001', '/dashboard/workplace', '仪表盘工作台', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 表单子权限
('550e8400-e29b-41d4-a716-446655440201', '基础表单', 'form:base', 2, '550e8400-e29b-41d4-a716-446655440002', '/form/base-form', '基础表单页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440202', '分步表单', 'form:step', 2, '550e8400-e29b-41d4-a716-446655440002', '/form/step-form', '分步表单页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440203', '高级表单', 'form:advanced', 2, '550e8400-e29b-41d4-a716-446655440002', '/form/advanced-form', '高级表单页', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 列表子权限
('550e8400-e29b-41d4-a716-446655440301', '表格列表', 'table:table-list', 2, '550e8400-e29b-41d4-a716-446655440003', '/list/table-list', '表格列表页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440302', '基础列表', 'table:basic-list', 2, '550e8400-e29b-41d4-a716-446655440003', '/list/basic-list', '基础列表页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440303', '卡片列表', 'table:card', 2, '550e8400-e29b-41d4-a716-446655440003', '/list/card', '卡片列表页', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440304', '搜索列表', 'table:search', 2, '550e8400-e29b-41d4-a716-446655440003', '/list/search', '搜索列表页', 4, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 个人中心子权限
('550e8400-e29b-41d4-a716-446655440401', '基本信息', 'profile:basic', 2, '550e8400-e29b-41d4-a716-446655440004', '/profile/basic', '个人基本信息页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440402', '高级设置', 'profile:advanced', 2, '550e8400-e29b-41d4-a716-446655440004', '/profile/advanced', '个人高级设置页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 结果页子权限
('550e8400-e29b-41d4-a716-446655440501', '成功页', 'result:success', 2, '550e8400-e29b-41d4-a716-446655440005', '/result/success', '成功结果页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440502', '失败页', 'result:fail', 2, '550e8400-e29b-41d4-a716-446655440005', '/result/fail', '失败结果页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 异常页子权限
('550e8400-e29b-41d4-a716-446655440601', '403页面', 'exception:403', 2, '550e8400-e29b-41d4-a716-446655440006', '/exception/403', '403权限不足页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440602', '404页面', 'exception:404', 2, '550e8400-e29b-41d4-a716-446655440006', '/exception/404', '404页面未找到', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440603', '500页面', 'exception:500', 2, '550e8400-e29b-41d4-a716-446655440006', '/exception/500', '500服务器错误页', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 用户管理子权限
('550e8400-e29b-41d4-a716-446655440701', '用户中心', 'user:center', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/center', '用户中心页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440702', '设置', 'user:settings', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings', '用户设置页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440703', '基本设置', 'user:settings-basic', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings/basic', '基本设置页', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440704', '安全设置', 'user:settings-security', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings/security', '安全设置页', 4, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440705', '个性化设置', 'user:settings-custom', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings/custom', '个性化设置页', 5, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440706', '绑定设置', 'user:settings-binding', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings/binding', '绑定设置页', 6, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440707', '通知设置', 'user:settings-notification', 2, '550e8400-e29b-41d4-a716-446655440007', '/account/settings/notification', '通知设置页', 7, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),

-- 支持子权限
('550e8400-e29b-41d4-a716-446655440801', '图标选择', 'support:icon-selector', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/icon-selector', '图标选择页', 1, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440802', '树目录表格', 'support:tree-list', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/tree-list', '树目录表格页', 2, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440803', '内联编辑表格', 'support:edit-table', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/edit-table', '内联编辑表格页', 3, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440804', '用户列表', 'support:user-list', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/user-list', '用户列表页', 4, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440805', '角色列表', 'support:role-list', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/role-list', '角色列表页', 5, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440806', '系统角色', 'support:system-role', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/system-role', '系统角色页', 6, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440807', '权限列表', 'support:permission-list', 2, '550e8400-e29b-41d4-a716-446655440008', '/other/list/permission-list', '权限列表页', 7, 1,
 @ADMIN_USER_ID, @ADMIN_USER_ID, NOW(), NOW(), NOW())

ON DUPLICATE KEY UPDATE 
    name = VALUES(name), 
    description = VALUES(description),
    sort_order = VALUES(sort_order),
    status = VALUES(status),
    modified_by = VALUES(modified_by),
    modified_time = NOW(),
    updated_time = NOW();

-- ========================================================
-- 3. 为超级管理员角色分配所有权限
-- ========================================================

-- 超级管理员角色ID
SET @SUPER_ADMIN_ROLE_ID = '550e8400-e29b-41d4-a716-446655440001';

-- 为超级管理员分配所有权限
INSERT INTO basicdata_role_permission (
    id, role_id, permission_id,
    created_by, modified_by, created_time, modified_time, updated_time
)
SELECT 
    UUID() as id,
    @SUPER_ADMIN_ROLE_ID as role_id,
    p.id as permission_id,
    @ADMIN_USER_ID as created_by,
    @ADMIN_USER_ID as modified_by,
    NOW() as created_time,
    NOW() as modified_time,
    NOW() as updated_time
FROM basicdata_permission p
ON DUPLICATE KEY UPDATE 
    modified_by = VALUES(modified_by),
    modified_time = NOW(),
    updated_time = NOW();

-- ========================================================
-- 说明：
-- 1. type=1 表示菜单级权限（一级菜单）
-- 2. type=2 表示页面级权限（二级及以下页面）
-- 3. parent_id 为空表示顶级权限，非空表示子权限
-- 4. 使用 ON DUPLICATE KEY UPDATE 避免重复插入
-- 5. 权限ID采用UUID格式，便于分布式系统使用
-- ========================================================
