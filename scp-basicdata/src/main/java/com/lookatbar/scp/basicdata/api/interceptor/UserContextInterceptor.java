package com.lookatbar.scp.basicdata.api.interceptor;

import com.lookatbar.scp.basicdata.domain.model.permission.Permission;
import com.lookatbar.scp.basicdata.domain.model.role.Role;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.domain.service.UserDomainService;
import com.lookatbar.scp.basicdata.infrastructure.context.UserInfo;
import com.lookatbar.scp.basicdata.infrastructure.context.UserContext;
import com.lookatbar.scp.basicdata.infrastructure.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户上下文拦截器
 * 从请求头 Authorization 中解析 JWT token，提取用户信息并设置到 UserContext
 */
@Component
@RequiredArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor {

    /**
     * JWT工具类
     */
    private final JwtUtil jwtUtil;

    /**
     * 用户仓储
     */
    private final UserRepository userRepository;

    /**
     * 用户领域服务
     */
    private final UserDomainService userDomainService;

    /**
     * 请求前置拦截处理
     * 解析 Authorization header，设置用户上下文
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  请求处理器
     * @return true允许请求继续，false拒绝请求
     * @throws Exception 拦截过程中的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                
                // 获取用户角色和权限
                List<Role> roles = userDomainService.getUserRoles(userId);
                List<Permission> permissions = userDomainService.getUserPermissions(userId);
                
                List<String> roleCodes = roles.stream()
                        .map(Role::getCode)
                        .collect(Collectors.toList());
                
                List<String> permissionCodes = permissions.stream()
                        .map(Permission::getCode)
                        .collect(Collectors.toList());
                
                // 获取用户真实姓名
                String realName = userRepository.findById(userId)
                        .map(user -> user.getRealName())
                        .orElse(username);
                
                // 构建用户信息并设置到上下文
                UserInfo userInfo = UserInfo.builder()
                        .userId(userId)
                        .username(username)
                        .realName(realName)
                        .token(token)
                        .roles(roleCodes)
                        .permissions(permissionCodes)
                        .build();
                
                UserContext.setUserInfo(userInfo);
                
                // 设置请求头 X-User-Id，供权限拦截器使用
                request.setAttribute("X-User-Id", userId);
            }
        }
        
        return true;
    }

    /**
     * 请求后置处理
     * 清除用户上下文，防止内存泄漏
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  请求处理器
     * @param ex       异常（如果有）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}