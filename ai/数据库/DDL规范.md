【MySQL数据库规范总则】
1. 环境适配：MySQL 8.0+，存储引擎统一 InnoDB，字符集 utf8mb4，排序规则 utf8mb4_unicode_ci；
2. 命名规则：库名=应用名（小写），表名=业务前缀_场景（小写+下划线，单数），字段名=语义明确（小写+下划线），禁止保留字、拼音、不规范缩写；
3. 字段约束：
   * 主键统一使用UUID，使用CHAR(36)类型，
   * 必含 created_by(创建者)/modified_by(修改者)，CHAR(36) 必填字段 NOT NULL
   * 必含创建时间 created_time， DATETIME 类型，NOT NULL DEFAULT CURRENT_TIMESTAMP，插入数据时自动填充当前时间
   * 必含修改时间 modified_time， DATETIME 类型，CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP，插入数据时自动填充当前时间
   * 必含更新时间 updated_time， DATETIME(3) 类型，CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)，插入数据时自动填充当前时间
   * 小数用 DECIMAL，使用DECIMAL(18,6)，默认值为0.00
   * 布尔用 is_xxx+unsigned tinyint(1)
4. 索引规范：主键 pk_字段名、唯一索引 uk_字段名、普通索引 idx_字段名，单表索引≤5个，组合索引遵循最左匹配，VARCHAR 索引指定长度；
5. SQL 规范：禁止 SELECT *、${} 拼接、左模糊查询，批量操作优先 BATCH，分页优化深分页，联表不超过3张；
6. 分库分表：单表行数≥500万/容量≥2GB 才考虑，优先范围分表，未达阈值禁止提前分表；
7. 注释规范：表、字段、索引必须加 COMMENT，状态字段说明取值范围，变更字段同步更新注释。