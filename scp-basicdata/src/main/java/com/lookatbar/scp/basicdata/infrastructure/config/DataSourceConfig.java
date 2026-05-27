package com.lookatbar.scp.basicdata.infrastructure.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置类
 * 使用Druid连接池配置数据库连接
 */
@Configuration
public class DataSourceConfig {

    /**
     * 数据库连接URL
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * 数据库用户名
     */
    @Value("${spring.datasource.username}")
    private String username;

    /**
     * 数据库密码
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 数据库驱动类名
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 创建Druid数据源Bean
     *
     * @return DataSource实例
     */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }
}