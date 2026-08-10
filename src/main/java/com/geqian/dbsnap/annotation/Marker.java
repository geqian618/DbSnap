package com.geqian.dbsnap.annotation;

import java.lang.annotation.*;

/**
 * @author geqian
 * @date 1:00 2026/8/11
 */
// 1. 定义该注解只能用于类或接口
@Target(ElementType.TYPE)
// 2. 定义该注解在运行时也保留，以便通过反射读取
@Retention(RetentionPolicy.RUNTIME)
// 3. (可选) 让该注解能被 javadoc 工具记录
@Documented
public @interface Marker {
    String value();
}
