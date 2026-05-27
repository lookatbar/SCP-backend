package com.lookatbar.scp.basicdata.api.interceptor;

import com.lookatbar.scp.basicdata.api.annotation.RequiresPermission;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 权限拦截器
 * 用于拦截API请求，校验用户是否具有访问权限
 * 
 * <p>工作流程：
 * <ol>
 *   <li>检查请求是否为Controller方法调用</li>
 *   <li>查找方法或类上的@RequiresPermission注解</li>
 *   <li>从请求头获取用户ID</li>
 *   <li>校验用户是否具有所需权限</li>
 *   <li>根据校验结果允许或拒绝请求</li>
 * </ol>
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    /**
     * Spring应用上下文，用于动态获取Bean（解决循环依赖）
     */
    private final ApplicationContext applicationContext;

    /**
     * 构造函数
     * 使用@Lazy注解延迟加载，解决循环依赖问题
     *
     * @param applicationContext Spring应用上下文
     */
    public PermissionInterceptor(@Lazy ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 动态获取UserApplicationService
     * 通过ApplicationContext动态获取Bean，避免循环依赖
     *
     * @return UserApplicationService实例
     */
    private UserApplicationService getUserApplicationService() {
        return applicationContext.getBean(UserApplicationService.class);
    }

    /**
     * 请求前置拦截处理
     * 校验用户权限，决定是否允许请求继续执行
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  请求处理器
     * @return true允许请求继续，false拒绝请求
     * @throws Exception 拦截过程中的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非Controller方法直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 查找方法级别的权限注解
        RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
        
        // 如果方法级别没有，查找类级别的权限注解
        if (requiresPermission == null) {
            requiresPermission = handlerMethod.getBeanType().getAnnotation(RequiresPermission.class);
        }

        // 没有权限注解则直接放行
        if (requiresPermission == null) {
            return true;
        }

        // 从请求头获取用户ID
        String userId = request.getHeader("X-User-Id");
        if (userId == null || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String[] permissionCodes = requiresPermission.value();
        RequiresPermission.Logical logical = requiresPermission.logical();

        // 根据逻辑运算符校验权限
        boolean hasPermission;
        if (logical == RequiresPermission.Logical.AND) {
            hasPermission = checkAllPermissions(userId, permissionCodes);
        } else {
            hasPermission = checkAnyPermission(userId, permissionCodes);
        }

        // 权限不足时拒绝请求
        if (!hasPermission) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    /**
     * 检查用户是否具有所有指定权限（AND逻辑）
     *
     * @param userId         用户ID
     * @param permissionCodes 权限编码数组
     * @return 是否具有所有权限
     */
    private boolean checkAllPermissions(String userId, String[] permissionCodes) {
        UserApplicationService service = getUserApplicationService();
        for (String code : permissionCodes) {
            if (!service.hasPermission(userId, code)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查用户是否具有任一指定权限（OR逻辑）
     *
     * @param userId         用户ID
     * @param permissionCodes 权限编码数组
     * @return 是否具有任一权限
     */
    private boolean checkAnyPermission(String userId, String[] permissionCodes) {
        UserApplicationService service = getUserApplicationService();
        for (String code : permissionCodes) {
            if (service.hasPermission(userId, code)) {
                return true;
            }
        }
        return false;
    }
}