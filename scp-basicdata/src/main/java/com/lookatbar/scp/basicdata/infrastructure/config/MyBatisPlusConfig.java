package com.lookatbar.scp.basicdata.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置类
 * 配置Mapper扫描路径
 */
@Configuration
@MapperScan("com.lookatbar.scp.basicdata.infrastructure.persistence.mapper")
public class MyBatisPlusConfig {
}