package com.lookatbar.scp.basicdata.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SpringDoc OpenAPI配置类
 * 配置API文档的基本信息和服务器信息
 */
@Configuration
public class OpenApiConfig {

    /**
     * 配置OpenAPI文档信息
     *
     * @return OpenAPI配置实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SCP基础数据模块 API")
                        .version("1.0.0")
                        .description("SCP基础数据模块提供RBAC3权限管理功能，包括用户管理、角色管理、权限管理等核心功能")
                        .contact(new Contact()
                                .name("SCP开发团队")
                                .email("dev@lookatbar.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://127.0.0:8101")
                                .description("本地开发服务器")
                ));
    }
}
