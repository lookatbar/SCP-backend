【数据库规范自检清单】
请检查以下建表 SQL/表结构，违规立即修正，只输出最终合规的 DDL 脚本：
1.  库表/字段命名是否小写+下划线，无保留字、拼音、不规范缩写，表名是否为单数；
2.  主键是否为 CHAR(36)，是否包含 created_by/modified_by/created_time/modified_time/updated_time 字段；
3.  字段类型是否合规：小数用 DECIMAL、布尔用 is_xxx+unsigned tinyint(1)、日期用 DATETIME；
4.  索引命名是否符合 pk/uk/idx_ 规则，业务唯一字段是否建唯一索引，单表索引是否≤5个；
5.  表引擎是否为 InnoDB，字符集是否为 utf8mb4，排序规则是否为 utf8mb4_unicode_ci；
6.  表、字段、索引是否有完整 COMMENT，状态字段是否说明取值范围；
7.  有无冗余字段、不合理默认值，必填字段是否加 NOT NULL 约束；
8.  联表关联字段类型是否一致，是否建索引；
9.  银行场景金额字段是否合规：是否使用 DECIMAL(18,6) 类型，禁止 FLOAT/DOUBLE/BIGINT，，是否非空且默认值为0.00，注释是否明确单位及合规依据。