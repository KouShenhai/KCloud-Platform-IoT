package io.laokou.log.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
}
