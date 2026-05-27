package com.lookatbar.scp.basicdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SCP基础数据模块启动类
 * 提供RBAC3权限管理功能，包括用户管理、角色管理、权限管理等核心功能
 * 
 * <p>模块架构：
 * <ul>
 *   <li>api层：Controller、Interceptor、Annotation</li>
 *   <li>application层：ApplicationService、Assembler、DTO</li>
 *   <li>domain层：Entity、Aggregate、DomainService、Repository接口</li>
 *   <li>infrastructure层：Repository实现、Config、Mapper、PO</li>
 * </ul>
 */
@SpringBootApplication(scanBasePackages = "com.lookatbar.scp.basicdata")
public class BasicDataApplication {

    /**
     * 应用程序入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(BasicDataApplication.class, args);
    }
}
