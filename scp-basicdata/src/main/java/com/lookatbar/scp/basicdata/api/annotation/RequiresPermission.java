package com.lookatbar.scp.basicdata.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {

    String[] value() default {};

    Logical logical() default Logical.AND;

    enum Logical {
        AND,
        OR
    }
}