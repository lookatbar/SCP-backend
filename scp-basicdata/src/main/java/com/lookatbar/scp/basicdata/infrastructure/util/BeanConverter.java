package com.lookatbar.scp.basicdata.infrastructure.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;

import java.lang.reflect.Constructor;
import java.util.function.Function;

/**
 * Bean 转换工具类
 * 基于 Hutool 的 BeanUtil 实现领域模型与持久化对象之间的转换
 * 简化 Repository 中的转换逻辑，减少冗余代码
 */
public class BeanConverter {

    /**
     * 默认的 CopyOptions，忽略 null 值
     */
    private static final CopyOptions DEFAULT_OPTIONS = CopyOptions.create()
            .setIgnoreNullValue(true)
            .setIgnoreError(true);

    /**
     * 将源对象转换为目标类型
     * 使用 Hutool 的 BeanUtil 进行属性复制
     * 如果 BeanUtil 创建对象失败，尝试使用反射创建
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标类型实例
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        
        try {
            // 尝试使用 BeanUtil 进行转换
            T result = BeanUtil.copyProperties(source, targetClass);
            
            // 如果 BeanUtil 返回 null，尝试使用反射创建实例
            if (result == null) {
                result = createInstance(targetClass);
                if (result != null) {
                    BeanUtil.copyProperties(source, result, DEFAULT_OPTIONS);
                }
            }
            
            return result;
        } catch (Exception e) {
            // 如果 BeanUtil 转换失败，尝试使用反射方式
            try {
                T result = createInstance(targetClass);
                if (result != null) {
                    BeanUtil.copyProperties(source, result, DEFAULT_OPTIONS);
                }
                return result;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 使用反射创建目标类实例
     * 优先尝试无参构造函数，其次尝试全参构造函数
     *
     * @param targetClass 目标类型
     * @param <T>         目标类型
     * @return 目标类型实例
     */
    private static <T> T createInstance(Class<T> targetClass) throws Exception {
        // 优先尝试无参构造函数
        try {
            Constructor<T> constructor = targetClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            // 无参构造函数不存在，尝试其他方式
        }
        
        // 尝试获取所有构造函数，选择参数最多的一个
        Constructor<?>[] constructors = targetClass.getDeclaredConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            
            // 获取构造函数参数类型
            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] params = new Object[paramTypes.length];
            
            // 为每个参数创建默认值
            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = getDefaultValue(paramTypes[i]);
            }
            
            @SuppressWarnings("unchecked")
            T result = (T) constructor.newInstance(params);
            return result;
        }
        
        return null;
    }

    /**
     * 获取指定类型的默认值
     *
     * @param type 类型
     * @return 默认值
     */
    private static Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            if (type == byte.class || type == short.class || type == int.class || type == long.class) {
                return 0;
            }
            if (type == float.class || type == double.class) {
                return 0.0;
            }
        }
        return null;
    }

    /**
     * 将源对象转换为目标类型，并应用自定义转换逻辑
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param converter   自定义转换函数
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return 目标类型实例
     */
    public static <S, T> T convert(S source, Class<T> targetClass, Function<S, T> converter) {
        if (source == null) {
            return null;
        }
        return converter.apply(source);
    }

    /**
     * 将源对象复制到已存在的目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源类型
     * @param <T>    目标类型
     * @return 目标对象
     */
    public static <S, T> T copy(S source, T target) {
        if (source == null || target == null) {
            return target;
        }
        BeanUtil.copyProperties(source, target, DEFAULT_OPTIONS);
        return target;
    }

    /**
     * 将源对象复制到已存在的目标对象，使用自定义选项
     *
     * @param source  源对象
     * @param target  目标对象
     * @param options 复制选项
     * @param <S>     源类型
     * @param <T>     目标类型
     * @return 目标对象
     */
    public static <S, T> T copy(S source, T target, CopyOptions options) {
        if (source == null || target == null) {
            return target;
        }
        BeanUtil.copyProperties(source, target, options);
        return target;
    }

    /**
     * 创建 CopyOptions 构建器
     * 用于自定义属性复制规则
     *
     * @return CopyOptions 实例
     */
    public static CopyOptions createOptions() {
        return CopyOptions.create();
    }
}