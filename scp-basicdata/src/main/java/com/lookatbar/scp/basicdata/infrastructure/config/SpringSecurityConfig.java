package com.lookatbar.scp.basicdata.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 配置安全策略、CSRF防护和会话管理
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * 配置安全过滤链
     * - 禁用CSRF（RESTful API不需要）
     * - 使用无状态会话策略
     * - 放行登录和注册接口
     *
     * @param http HttpSecurity配置
     * @return SecurityFilterChain实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                .anyRequest().permitAll()
            );
        return http.build();
    }
}