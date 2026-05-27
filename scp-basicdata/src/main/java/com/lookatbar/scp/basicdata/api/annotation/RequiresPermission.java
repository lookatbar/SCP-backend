package com.lookatbar.scp.basicdata.api.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 用于标记需要权限校验的Controller方法或类
 * 
 * <p>支持两种逻辑运算符：
 * <ul>
 *   <li>AND - 必须拥有所有指定权限</li>
 *   <li>OR - 拥有任一指定权限即可</li>
 * </ul>
 * 
 * <p>使用示例：
 * <pre>
 * {@code @RequiresPermission("user:create")}
 * {@code @RequiresPermission(value = {"user:read", "user:write"}, logical = Logical.OR)}
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {

    /**
     * 权限编码数组
     * 
     * @return 权限编码列表
     */
    String[] value() default {};

    /**
     * 权限校验逻辑运算符
     * 
     * @return AND或OR，默认为AND
     */
    Logical logical() default Logical.AND;

    /**
     * 逻辑运算符枚举
     */
    enum Logical {
        /**
         * 与逻辑：必须满足所有权限
         */
        AND,
        /**
         * 或逻辑：满足任一权限即可
         */
        OR
    }
}