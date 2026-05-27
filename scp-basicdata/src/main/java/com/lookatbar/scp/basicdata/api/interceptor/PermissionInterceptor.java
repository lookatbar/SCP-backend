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

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;

    public PermissionInterceptor(@Lazy ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private UserApplicationService getUserApplicationService() {
        return applicationContext.getBean(UserApplicationService.class);
    }

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
            hasPermission = checkAllPermissions(userId, permissionCodes);
        } else {
            hasPermission = checkAnyPermission(userId, permissionCodes);
        }

        if (!hasPermission) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private boolean checkAllPermissions(String userId, String[] permissionCodes) {
        UserApplicationService service = getUserApplicationService();
        for (String code : permissionCodes) {
            if (!service.hasPermission(userId, code)) {
                return false;
            }
        }
        return true;
    }

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