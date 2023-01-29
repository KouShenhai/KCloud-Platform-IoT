package org.laokou.common.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * @author laokou
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {
    /**
     * 数据源名称 master为默认数据源
     * @return
     */
    String value() default "master";

}
