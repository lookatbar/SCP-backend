package com.lookatbar.scp.basicdata.infrastructure.config;

import com.lookatbar.scp.basicdata.api.interceptor.PermissionInterceptor;
import com.lookatbar.scp.basicdata.api.interceptor.UserContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 注册拦截器
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * 用户上下文拦截器
     */
    private final UserContextInterceptor userContextInterceptor;

    /**
     * 权限拦截器
     */
    private final PermissionInterceptor permissionInterceptor;

    /**
     * 注册拦截器
     * 
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户上下文拦截器（优先级最高，最先执行）
        registry.addInterceptor(userContextInterceptor)
                .addPathPatterns("/api/**")
                .order(1);

        // 权限拦截器（优先级次高）
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/users/login",
                        "/api/users/register",
                        "/api/error"
                )
                .order(2);
    }
}