package com.lookatbar.scp.basicdata.infrastructure.context;

/**
 * 用户上下文
 * 使用 ThreadLocal 存储当前请求的用户信息
 * 在拦截器中设置，在 Controller/Service 中获取
 */
public class UserContext {

    /**
     * ThreadLocal 存储用户信息
     */
    private static final ThreadLocal<UserInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     * 
     * @param userInfo 用户信息
     */
    public static void setUserInfo(UserInfo userInfo) {
        USER_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取当前用户信息
     * 
     * @return 用户信息，如果未登录则返回 null
     */
    public static UserInfo getUserInfo() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 获取当前用户ID
     * 
     * @return 用户ID，如果未登录则返回 null
     */
    public static String getUserId() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * 获取当前用户名
     * 
     * @return 用户名，如果未登录则返回 null
     */
    public static String getUsername() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * 获取当前用户真实姓名
     * 
     * @return 真实姓名，如果未登录则返回 null
     */
    public static String getRealName() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getRealName() : null;
    }

    /**
     * 获取当前用户角色列表
     * 
     * @return 角色列表，如果未登录则返回空列表
     */
    public static java.util.List<String> getRoles() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getRoles() : java.util.Collections.emptyList();
    }

    /**
     * 获取当前用户权限列表
     * 
     * @return 权限列表，如果未登录则返回空列表
     */
    public static java.util.List<String> getPermissions() {
        UserInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getPermissions() : java.util.Collections.emptyList();
    }

    /**
     * 检查当前用户是否已登录
     * 
     * @return 是否已登录
     */
    public static boolean isLoggedIn() {
        return getUserInfo() != null;
    }

    /**
     * 检查当前用户是否具有指定角色
     * 
     * @param roleCode 角色编码
     * @return 是否具有该角色
     */
    public static boolean hasRole(String roleCode) {
        return getRoles().contains(roleCode);
    }

    /**
     * 检查当前用户是否具有指定权限
     * 
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public static boolean hasPermission(String permissionCode) {
        return getPermissions().contains(permissionCode);
    }

    /**
     * 清除当前用户信息
     * 在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }
}