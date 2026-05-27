# RBAC3 数据库初始化指南

## 概述

本文档说明如何初始化 RBAC3 权限管理系统的数据库。

## 前置条件

1. **MySQL 8.0+** 已安装并运行
2. **MySQL 客户端工具** 已安装（mysql 命令或 mysqlsh）
3. **root 权限** 用于创建数据库和用户

## 方法一：使用 PowerShell 脚本（推荐）

### 步骤

1. 打开 PowerShell（管理员权限）
2. 导航到脚本目录：
   ```powershell
   cd d:\project\java\scp\SCP-backend\ci
   ```

3. 执行初始化脚本：
   ```powershell
   .\init-database.ps1
   ```

4. 按提示输入 MySQL root 密码

### 脚本参数

```powershell
# 自定义参数示例
.\init-database.ps1 -host "192.168.1.100" -port "3307" -rootUser "root" -rootPassword "your_password"
```

**可用参数：**
- `-host`: MySQL 服务器地址（默认：127.0.0.1）
- `-port`: MySQL 端口（默认：3306）
- `-rootUser`: root 用户名（默认：root）
- `-rootPassword`: root 密码（默认：空，会提示输入）
- `-dbUser`: 数据库用户名（默认：lookatbar）
- `-dbPassword`: 数据库密码（默认：1）
- `-dbName`: 数据库名（默认：scp）

## 方法二：手动执行 SQL 脚本

### 步骤 1：创建数据库和用户

使用 root 用户登录 MySQL，执行：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `scp` 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER IF NOT EXISTS 'lookatbar'@'%' IDENTIFIED BY '1';

-- 授权
GRANT ALL PRIVILEGES ON scp.* TO 'lookatbar'@'%';

-- 刷新权限
FLUSH PRIVILEGES;
```

### 步骤 2：执行表结构脚本

使用命令行：
```bash
mysql -u lookatbar -p1 -h 127.0.0.1 scp < d:\project\java\scp\SCP-backend\ci\rbac-schema.sql
```

或使用 mysqlsh：
```bash
mysqlsh --user=lookatbar --password=1 --host=127.0.0.1 --port=3306 --database=scp --file=d:\project\java\scp\SCP-backend\ci\rbac-schema.sql
```

## 方法三：Spring Boot 自动初始化

项目已配置 Spring Boot 在启动时自动执行数据库初始化。

### 配置说明

在 `application.yaml` 中已配置：

```yaml
spring:
  sql:
    init:
      mode: always
      encoding: UTF-8
      continue-on-error: false
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql
```

### 启动应用

```bash
cd d:\project\java\scp\SCP-backend\scp-basicdata
mvn spring-boot:run
```

应用启动时会自动：
1. 创建所有表结构
2. 插入初始化数据（角色、权限）

## 验证安装

### 方法 1：检查表是否存在

```sql
USE scp;
SHOW TABLES;
```

应该看到以下表：
- rbac_user
- rbac_role
- rbac_permission
- rbac_user_role
- rbac_role_permission
- rbac_role_hierarchy
- rbac_role_mutually_exclusive

### 方法 2：检查初始化数据

```sql
-- 查看角色
SELECT * FROM rbac_role;

-- 查看权限
SELECT * FROM rbac_permission;
```

## 数据库规范符合性

所有表结构均遵循《MySQL 数据库规范总则》：

✅ **主键**：统一使用 UUID，CHAR(36) 类型  
✅ **必填字段**：created_by, modified_by (CHAR(36) NOT NULL)  
✅ **时间字段**：
- created_time: DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
- modified_time: DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
- updated_time: DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)

✅ **状态字段**：TINYINT(1) UNSIGNED (0-禁用，1-启用)  
✅ **索引命名**：
- 主键索引：pk_字段名
- 唯一索引：uk_字段名
- 普通索引：idx_字段名

✅ **存储引擎**：InnoDB  
✅ **字符集**：utf8mb4  
✅ **排序规则**：utf8mb4_unicode_ci  
✅ **注释**：所有表和字段都有 COMMENT  

## 常见问题

### Q1: 提示"MySQL 客户端未找到"

**解决方案**：
1. 安装 MySQL Server（包含 mysql 命令）
2. 或安装 MySQL Shell
3. 或将 MySQL bin 目录添加到 PATH 环境变量

### Q2: 权限不足错误

**解决方案**：
确保使用 root 用户或有 CREATE USER、CREATE DATABASE 权限的用户执行脚本。

### Q3: 数据库已存在

**解决方案**：
脚本使用 `IF NOT EXISTS` 语法，不会重复创建。如需重新初始化，先删除数据库：
```sql
DROP DATABASE IF EXISTS scp;
```

### Q4: Spring Boot 启动时报 SQL 错误

**解决方案**：
1. 检查数据库连接配置是否正确
2. 确保数据库已创建
3. 查看应用日志获取详细错误信息
4. 可以临时设置 `spring.sql.init.mode=never` 禁用自动初始化

## 文件清单

```
SCP-backend/ci/
├── rbac-schema.sql          # 完整的表结构 SQL（含初始化数据）
├── init-database.ps1        # PowerShell 自动化脚本
├── init-database.sql        # 数据库和用户创建脚本
└── README-数据库初始化.md    # 本文档

SCP-backend/scp-basicdata/src/main/resources/db/
├── schema.sql               # 表结构脚本（Spring Boot 自动执行）
└── data.sql                 # 初始化数据脚本（Spring Boot 自动执行）
```

## 下一步

数据库初始化完成后，可以：

1. 启动 Spring Boot 应用
2. 访问 API 接口测试权限功能
3. 使用 Postman 或其他工具测试 RBAC 功能

## 技术支持

如有问题，请联系开发团队或查看项目文档。
