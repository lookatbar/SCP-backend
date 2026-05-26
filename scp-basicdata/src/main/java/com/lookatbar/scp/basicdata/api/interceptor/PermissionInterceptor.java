package com.lookatbar.scp.basicdata.api.interceptor;

import com.lookatbar.scp.basicdata.api.annotation.RequiresPermission;
import com.lookatbar.scp.basicdata.application.service.UserApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final UserApplicationService userApplicationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
        if (requiresPermission == null) {
            requiresPermission = handlerMethod.getBeanType().getAnnotation(RequiresPermission.class);
        }

        if (requiresPermission == null) {
            return true;
        }

        String userId = request.getHeader("X-User-Id");
        if (userId == null || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String[] permissionCodes = requiresPermission.value();
        RequiresPermission.Logical logical = requiresPermission.logical();

        boolean hasPermission;
        if (logical == RequiresPermission.Logical.AND) {
            hasPermission = checkAllPermissions(Long.parseLong(userId), permissionCodes);
        } else {
            hasPermission = checkAnyPermission(Long.parseLong(userId), permissionCodes);
        }

        if (!hasPermission) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private boolean checkAllPermissions(Long userId, String[] permissionCodes) {
        for (String code : permissionCodes) {
            if (!userApplicationService.hasPermission(userId, code)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAnyPermission(Long userId, String[] permissionCodes) {
        for (String code : permissionCodes) {
            if (userApplicationService.hasPermission(userId, code)) {
                return true;
            }
        }
        return false;
    }
}