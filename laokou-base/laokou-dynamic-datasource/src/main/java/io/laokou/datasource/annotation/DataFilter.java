package io.laokou.datasource.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 * @author Kou Shenhai
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    /**
     * 表别名
     */
    String tableAlias() default "";

    /**
     * 用户ID
     */
    String userId() default "creator";

}
