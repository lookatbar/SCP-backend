package com.lookatbar.scp.basicdata.infrastructure.config;

import com.lookatbar.scp.basicdata.api.interceptor.PermissionInterceptor;
import com.lookatbar.scp.basicdata.api.interceptor.UserContextInterceptor;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类
 * 配置密码编码器、权限拦截器和跨域设置
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * 权限拦截器
     */
    private final PermissionInterceptor permissionInterceptor;

    /**
     * 配置密码编码器
     * 使用BCrypt算法进行密码加密
     *
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册权限拦截器
     * 拦截所有/api/**请求，排除登录、注册接口和Swagger文档接口
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/users/login", 
                        "/api/users/register",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**"
                );


    }

    /**
     * 配置跨域资源共享(CORS)
     * 允许所有来源访问/api/**接口
     *
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}