package io.laokou.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 * @author Kou Shenhai
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value();
}
