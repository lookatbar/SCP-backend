package com.lookatbar.scp.basicdata.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lookatbar.scp.basicdata.infrastructure.persistence.mapper")
public class MyBatisPlusConfig {
}